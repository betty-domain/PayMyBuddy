package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFriendTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class FriendTransactionControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IFriendTransactionService friendTransactionService;

    @Test
    public void transferToFriendStatusOK() throws Exception
    {
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(5);
        incomingTransactionDto.setBeneficiaryId(20);
        incomingTransactionDto.setDescription("description transaction");
        incomingTransactionDto.setAmount(new BigDecimal(250.45));

        when(friendTransactionService.transferToFriend(incomingTransactionDto)).thenReturn(new TransactionDto());


        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transaction").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(incomingTransactionDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void transferToFriendWithException() throws Exception
    {
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(5);
        incomingTransactionDto.setBeneficiaryId(20);
        incomingTransactionDto.setDescription("description transaction");
        incomingTransactionDto.setAmount(new BigDecimal(250.45));

        given(friendTransactionService.transferToFriend(incomingTransactionDto)).willThrow(new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transaction").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(incomingTransactionDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }
}
