package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTests {

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
        user.setTransactionList(new ArrayList<>());
        user.setLastname("lastname");

        String result = new ObjectMapper().writeValueAsString(user);

        assertThat(result).contains("23");
        assertThat(result).contains("\"email\"");
        assertThat(result).contains("\"lastname\"");
        assertThat(result).contains("\"firstname\"");
        assertThat(result).contains("\"password\"");
        assertThat(result).contains("\"balance\"");
        assertThat(result).contains("\"friendshipList\"");

        assertThat(result).doesNotContain("\"bankAccountList\"");
        assertThat(result).doesNotContain("\"bankTransferList\"");
        assertThat(result).doesNotContain("\"transactionList\"");

    }
}
