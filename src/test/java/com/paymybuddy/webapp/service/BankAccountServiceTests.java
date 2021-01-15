package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.BankAccountDtoMapper;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankAccountServiceTests {

    @Autowired
    private IBankAccountService bankAccountService;

    @MockBean
    private BankAccountRepository bankAccountRepositoryMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;

    @Test
    public void addBankAccountWithNullDTO() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.addBankAccount(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void addBankAccountWithInvalidData() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban(null);

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.addBankAccount(bankAccountDto);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void addBankWithUnexistingUser() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("null");

        when(userRepositoryMock.findById(bankAccountDto.getUserId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.addBankAccount(bankAccountDto);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inconnu");
    }

    @Test
    void addBankAccount_IbanAlreadyExistForThisUser() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("FR12 3456 7654 21");

        when(userRepositoryMock.findById(bankAccountDto.getUserId())).thenReturn(Optional.of(new User()));
        when(bankAccountRepositoryMock.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId())).thenReturn(Optional.of(new BankAccount()));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.addBankAccount(bankAccountDto);
        });

        assertThat(exception.getMessage()).contains("Iban déjà existant pour cet utilisateur");
    }

    @Test
    void addValidBankAccount_NewBankAccount() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("FR12 3456 7654 21");
        bankAccountDto.setId(null);

        BankAccount createdBankAccount = new BankAccount();
        createdBankAccount.setActif(true);
        createdBankAccount.setDescription(bankAccountDto.getDescription());
        createdBankAccount.setIban(bankAccountDto.getIban());
        createdBankAccount.setId(43);
        createdBankAccount.setUser(new User());

        when(userRepositoryMock.findById(bankAccountDto.getUserId())).thenReturn(Optional.of(new User()));
        when(bankAccountRepositoryMock.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId())).thenReturn(Optional.empty());
        when(bankAccountRepositoryMock.save(bankAccountDtoMapper.mapToBankAccount(bankAccountDto, new User()))).thenReturn(createdBankAccount);

        assertThat(bankAccountService.addBankAccount(bankAccountDto).getId()).isNotNull();
        verify(bankAccountRepositoryMock,Mockito.times(1)).save(any());

    }

    @Test
    void addValidBankAccount_desactivatedBankAccount() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("FR12 3456 7654 21");
        bankAccountDto.setId(null);

        BankAccount createdBankAccount = new BankAccount();
        createdBankAccount.setActif(false);
        createdBankAccount.setDescription("old description");
        createdBankAccount.setIban(bankAccountDto.getIban());
        createdBankAccount.setId(43);
        User user=new User();
        user.setId(42);
        createdBankAccount.setUser(user);

        when(userRepositoryMock.findById(bankAccountDto.getUserId())).thenReturn(Optional.of(new User()));
        when(bankAccountRepositoryMock.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId())).thenReturn(Optional.of(createdBankAccount));

        BankAccountDto createdBankAccountDto =bankAccountService.addBankAccount(bankAccountDto);

        assertThat(createdBankAccountDto.isValid()).isTrue();
        assertThat(createdBankAccountDto.getId()).isEqualTo(createdBankAccount.getId());
        verify(bankAccountRepositoryMock,Mockito.times(1)).save(any());

    }

    @Test
    void deleteBankAccountWithIdNull() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.desactivateBankAccount(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void deleteBankAccount_NonExistingBankAccount()
    {
        int idBankAccount = 15;
        when(bankAccountRepositoryMock.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.desactivateBankAccount(idBankAccount);
        });

        assertThat(exception.getMessage()).contains("Compte bancaire inexistant");
    }

    @Test
    void deleteBankAccountValid()
    {
        int idBankAccount = 15;

        when(bankAccountRepositoryMock.findById(any())).thenReturn(Optional.of(new BankAccount()));
        assertThat(bankAccountService.desactivateBankAccount(idBankAccount)).isTrue();
        verify(bankAccountRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    void getBankAccountListForUser_NullUserId()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.getBankAccountListForUser(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void getBankAccountListForUser_NonExistingUser()
    {
        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.getBankAccountListForUser(15);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void getBankAccountListForUser_WithUnactiveBankAccount()
    {
        User user = new User();
        user.setId(25);
        BankAccount bankAccount = new BankAccount(5,"iban1",false,"descrption1",user);
        BankAccount bankAccount1 = new BankAccount(50,"iban2",true,"descrption2",user);
        BankAccount bankAccount2 = new BankAccount(500,"iban3",false,"descrption3",user);
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount);
        bankAccountList.add(bankAccount1);
        bankAccountList.add(bankAccount2);
        user.setBankAccountList(bankAccountList);

        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.of(user));

        List<BankAccountDto> bankAccountDtoList = bankAccountService.getBankAccountListForUser(user.getId());

        assertThat(bankAccountDtoList.size()).isEqualTo(1);
        assertThat(bankAccountDtoList.get(0).getId()).isEqualTo(bankAccount1.getId());
    }
}
