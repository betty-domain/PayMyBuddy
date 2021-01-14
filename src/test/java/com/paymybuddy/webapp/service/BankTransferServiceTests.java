package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.dto.BankTransferListDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.BankTransferRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BankTransferServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private BankTransferRepository bankTransferRepository;

    @Autowired
    BankTransferService bankTransferService;

    @Test
    void transferFromBank_OK() {
        Integer userId = 55;
        Integer bankAccountId = 14;
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(bankAccountId);
        bankTransferDto.setAmount(new BigDecimal(250));

        User user = new User();
        user.setId(userId);
        user.setBalance(new BigDecimal(0));
        BankAccount bankAccount = new BankAccount(bankAccountId, "iban", true, "description", user);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankAccountId, userId)).thenReturn(Optional.of(bankAccount));
        BankTransfer bankTransfer = new BankTransfer();

        when(bankTransferRepository.save(any())).thenReturn(bankTransfer);

        BankTransfer savedBankTransfer = bankTransferService.transferFromBank(bankTransferDto);
        verify(bankTransferRepository, Mockito.times(1)).save(any());
    }

    @Test
    void transferFromBank_WithNullValues() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(null);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void transferFromBank_WithInvalidValues() {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(null);
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void transferFromBank_NotExistingBankAccountForUser() {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(new BigDecimal(25));
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(anyInt(), anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Compte inexistant pour cet utilisateur");

    }

    @Test
    void transferToBank_OK() {
        Integer userId = 55;
        Integer bankAccountId = 14;
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(bankAccountId);
        bankTransferDto.setAmount(new BigDecimal(250));

        User user = new User();
        user.setId(userId);
        user.setBalance(new BigDecimal(1000));
        BankAccount bankAccount = new BankAccount(bankAccountId, "iban", true, "description", user);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankAccountId, userId)).thenReturn(Optional.of(bankAccount));
        BankTransfer bankTransfer = new BankTransfer();

        when(bankTransferRepository.save(any())).thenReturn(bankTransfer);

        BankTransfer savedBankTransfer = bankTransferService.transferToBank(bankTransferDto);
        verify(bankTransferRepository, Mockito.times(1)).save(any());
    }

    @Test
    void transferToBank_WithNullValues() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(null);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void transferToBank_WithInvalidValues() {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(null);
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    void transferToBank_NotExistingBankAccountForUser() {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(new BigDecimal(25));
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(anyInt(), anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Compte inexistant pour cet utilisateur");

    }

    @Test
    void transferToBank_NotSufficientBalance() {
        Integer userId = 55;
        Integer bankAccountId = 14;
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(bankAccountId);
        bankTransferDto.setAmount(new BigDecimal(250));

        User user = new User();
        user.setId(userId);
        user.setBalance(new BigDecimal(10));
        BankAccount bankAccount = new BankAccount(bankAccountId, "iban", true, "description", user);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankAccountId, userId)).thenReturn(Optional.of(bankAccount));
        BankTransfer bankTransfer = new BankTransfer();

        when(bankTransferRepository.save(any())).thenReturn(bankTransfer);

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Solde Insuffisant");

    }

    @Test
    void getAllTransferForUser_Valid() {
        Integer userId = 40;
        //given un utilisateur
        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(userId);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionOutcomingList(new ArrayList<>());
        user.setTransactionIncomingList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        //given 1 compte bancaire avec 2 transferts d'argent
        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", user);
        user.getBankAccountList().add(bankAccount);
        List<BankTransfer> bankTransferList = new ArrayList<>();
        BankTransfer bankTransfer = new BankTransfer(452, new BigDecimal(54.25), LocalDate.of(2014, 3, 12), BankTransferOrder.FROM_BANK);
        bankTransfer.setBankAccount(bankAccount);
        bankTransfer.setUser(user);
        bankTransferList.add(bankTransfer);

        BankTransfer bankTransfer2 = new BankTransfer(220, new BigDecimal(5401), LocalDate.of(2010, 1, 1), BankTransferOrder.TO_BANK);
        bankTransfer2.setBankAccount(bankAccount);
        bankTransfer2.setUser(user);
        bankTransferList.add(bankTransfer2);

        bankAccount.setBankTransferList(bankTransferList);
        user.getBankTransferList().addAll(bankTransferList);

        //given 1 compte bancaire avec un transfert d'argent
        BankAccount bankAccount2 = new BankAccount(15, "iban", true, "description", user);
        user.getBankAccountList().add(bankAccount2);
        List<BankTransfer> bankTransferList_2 = new ArrayList<>();
        BankTransfer bankTransfer3 = new BankTransfer(45, new BigDecimal(54.25), LocalDate.of(2014, 3, 12), BankTransferOrder.FROM_BANK);
        bankTransfer3.setBankAccount(bankAccount2);
        bankTransfer3.setUser(user);
        bankTransferList_2.add(bankTransfer3);

        bankAccount2.setBankTransferList(bankTransferList_2);
        user.getBankTransferList().addAll(bankTransferList_2);

        //given 1 compte bancaire sans transfert d'argent
        BankAccount bankAccount3 = new BankAccount(151, "iban", true, "description", user);
        user.getBankAccountList().add(bankAccount3);

        user.getBankTransferList().addAll(bankTransferList);
        user.getBankTransferList().addAll(bankTransferList_2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BankTransferListDto bankTransferListDto = bankTransferService.getAllTransferForUser(userId);

        assertThat(bankTransferListDto.getUserDto().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(bankTransferListDto.getUserDto().getBalance()).isEqualTo(user.getBalance());

        List<BankAccountDto> bankAccountDtoList = bankTransferListDto.getBankAccountDtoList();

        assertThat(bankAccountDtoList.size()).isEqualTo(2);
        assertThat(bankAccountDtoList.stream().anyMatch(bankAccountDto -> bankAccountDto.getId().equals(bankAccount3.getId())));

        assertThat(bankAccountDtoList.get(0).getBankTransferDtoList().size()).isEqualTo(2);
        assertThat(bankAccountDtoList.get(1).getBankTransferDtoList().size()).isEqualTo(1);

    }

    @Test
    void getAllTransferForUser_UserNotExist() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.getAllTransferForUser(5);
        });

        Assertions.assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void getAllTransferForUser_UserIdNull() {

        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.getAllTransferForUser(null);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données invalides");
    }
}
