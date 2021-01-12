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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void addValidBankAccount() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setUserId(25);
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("FR12 3456 7654 21");

        BankAccount createdBankAccount = new BankAccount();
        createdBankAccount.setActif(true);
        createdBankAccount.setDescription(bankAccountDto.getDescription());
        createdBankAccount.setIban(bankAccountDto.getIban());
        createdBankAccount.setId(43);
        createdBankAccount.setUser(new User());

        when(userRepositoryMock.findById(bankAccountDto.getUserId())).thenReturn(Optional.of(new User()));
        when(bankAccountRepositoryMock.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId())).thenReturn(Optional.empty());
        when(bankAccountRepositoryMock.save(bankAccountDtoMapper.mapToBankAccount(bankAccountDto, new User()))).thenReturn(createdBankAccount);


        assertThat(bankAccountService.addBankAccount(bankAccountDto)).isEqualTo(createdBankAccount);
        verify(bankAccountRepositoryMock,Mockito.times(1)).save(any());

    }

    @Test
    void deleteBankAccountWithIdNull() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankAccountService.deleteBankAccount(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void deleteBankAccountValid()
    {
        int idBankAccount = 15;
        bankAccountService.deleteBankAccount(idBankAccount);
        verify(bankAccountRepositoryMock, Mockito.times(1)).deleteById(idBankAccount);
    }
}
