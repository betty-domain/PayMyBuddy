package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")/*,
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:removeData.sql")*/
})
public class BankAccountIT {
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void addBankAccountTest()
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setIban("DE 345 678 907");
        bankAccountDto.setDescription("new Iban from IT Test");
        Integer userId = 1;
        bankAccountDto.setUserId(userId);

        BankAccountDto createdBankAccount = bankAccountService.addBankAccount(bankAccountDto);

        assertThat(createdBankAccount.getId()).isNotNull();

    }

    @Test
    public void deleteBankAccount()
    {
        assertThat(bankAccountService.deleteBankAccount(1)).isTrue();
        BankAccount bankAccount = bankAccountRepository.findById(1).get();
        assertThat(bankAccount.isActif()).isFalse();

    }
}
