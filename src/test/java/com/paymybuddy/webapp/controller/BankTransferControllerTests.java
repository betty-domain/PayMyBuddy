package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.UserPrincipal;
import com.paymybuddy.webapp.service.IBankTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankTransferControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IBankTransferService bankTransferService;

    @Autowired
    private BankTransferController bankTransferController;

    @Test
    public void transferFromBankStatusOK() throws Exception
    {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setBankAccountId(20);
        bankTransferDto.setUserId(5);
        bankTransferDto.setAmount(new BigDecimal(250.45));

        when(bankTransferService.transferFromBank(bankTransferDto)).thenReturn(new BankTransfer());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transferFromBank").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankTransferDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void transferFromBankWithException() throws Exception
    {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setBankAccountId(20);
        bankTransferDto.setUserId(5);
        bankTransferDto.setAmount(new BigDecimal(250.45));

        given(bankTransferService.transferFromBank(bankTransferDto)).willThrow(new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transferFromBank").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankTransferDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }
}
