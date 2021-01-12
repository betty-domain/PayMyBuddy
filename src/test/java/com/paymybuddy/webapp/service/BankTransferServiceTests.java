package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankTransferServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private BankTransferRepository bankTransferRepository;

    @Autowired
    BankTransferService bankTransferService;

    @Test
    public void transferFromBank_OK() {
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
    public void transferFromBank_WithNullValues()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(null);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    public void transferFromBank_WithInvalidValues()
    {
        BankTransferDto  bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(null);
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    public void transferFromBank_NotExistingBankAccountForUser()
    {
        BankTransferDto  bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(new BigDecimal(25));
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(anyInt(),anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferFromBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Compte inexistant pour cet utilisateur");

    }

    @Test
    public void transferToBank_OK() {
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
    public void transferToBank_WithNullValues()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(null);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    public void transferToBank_WithInvalidValues()
    {
        BankTransferDto  bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(null);
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Données incorrectes");

    }

    @Test
    public void transferToBank_NotExistingBankAccountForUser()
    {
        BankTransferDto  bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(new BigDecimal(25));
        bankTransferDto.setBankAccountId(5);
        bankTransferDto.setUserId(10);

        when(bankAccountRepository.findByIdAndIsActifTrueAndUser_id(anyInt(),anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            bankTransferService.transferToBank(bankTransferDto);
        });

        Assertions.assertThat(exception.getMessage()).contains("Compte inexistant pour cet utilisateur");

    }

    @Test
    public void transferToBank_NotSuffisantBalance()
    {
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
}
