package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        user.setTransactionOutcomingList(new ArrayList<>());
        user.setTransactionIncomingList(new ArrayList<>());
        user.setLastname("lastname");


        ObjectMapper objectMapper = new ObjectMapper();
        String result = new ObjectMapper().writeValueAsString(user);
        JsonNode jsonNode = objectMapper.readTree(result);

        List<String> fieldsNames = Lists.newArrayList(jsonNode.fieldNames());

        assertThat(fieldsNames).containsAll(Arrays.asList(new String[]{"id","email","lastname","firstname","password","balance","friendshipList"}));
        assertThat(fieldsNames).doesNotContainAnyElementsOf(Arrays.asList(new String[]{"bankAccountList","bankTransferList","transactionList"}));

    }
}
