package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BankTransferTests {
    @Test
    public void bankTransferJsonIgnoreTests() throws JsonProcessingException
    {
        Integer userId= 52;
        Integer bankAccountId=45;
        Integer bankTransferId=250;

        User user = new User();
        user.setId(userId);
        user.setPassword("password");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setEmail("email");
        user.setBalance(new BigDecimal(52));
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setTransactionIncomingList(new ArrayList<>());
        user.setTransactionOutcomingList(new ArrayList<>());

        BankAccount bankAccount = new BankAccount(bankAccountId,"myIban",false,"description", user);
        user.getBankAccountList().add(bankAccount);

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setAmount(new BigDecimal(350.58));
        bankTransfer.setId(bankTransferId);
        bankTransfer.setTransferOrder(BankTransferOrder.TO_BANK);
        bankTransfer.setDate(LocalDateTime.of(2010,5,15,15,10,0));
        bankTransfer.setUser(user);
        bankTransfer.setBankAccount(bankAccount);


        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(bankTransfer);
        JsonNode jsonNode = objectMapper.readTree(result);

        List<String> fieldsNames = Lists.newArrayList(jsonNode.fieldNames());


        assertThat(fieldsNames).containsAll(Arrays.asList(new String[]{"id","amount","date","transferOrder","bankAccount"}));
        assertThat(fieldsNames).doesNotContainAnyElementsOf(Arrays.asList(new String[]{"user"}));
    }
}
