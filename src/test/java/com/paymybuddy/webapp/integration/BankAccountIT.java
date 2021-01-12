package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:removeData.sql")
})
public class BankAccountIT {
    @Autowired
    private BankAccountService bankAccountService;

    @Test
    public void addBankAccountTest()
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setIban("DE 345 678 907");
        bankAccountDto.setDescription("new Iban from IT Test");
        Integer userId = 1;
        bankAccountDto.setUserId(userId);

        BankAccount bankAccount = bankAccountService.addBankAccount(bankAccountDto);

        assertThat(bankAccount.getId()).isNotNull();
        assertThat(bankAccount.getUser().getId()).isEqualTo(userId);
    }

    @Test
    public void deleteBankAccount()
    {
        assertThat(bankAccountService.deleteBankAccount(1)).isTrue();
    }
}
