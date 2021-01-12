package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BankAccountTests {

    @Test
    public void BankAccountJsonIgnoreTests() throws JsonProcessingException
    {
        User user = new User();
        user.setId(23);
        user.setPassword("password");
        user.setFirstname("firstname");
        user.setEmail("email");
        user.setBalance(new BigDecimal(25));
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setLastname("lastname");

        BankAccount bankAccount = new BankAccount(150,"myIban",false,"description", user);

        user.getBankAccountList().add(bankAccount);

        String result = new ObjectMapper().writeValueAsString(bankAccount);

        assertThat(result).contains("150");
        assertThat(result).contains("\"iban\"");
        assertThat(result).doesNotContain("\"isActif\"");
        assertThat(result).contains("\"description\"");
        assertThat(result).contains("\"user\"");


    }
}
