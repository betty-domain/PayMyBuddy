package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.dto.BankTransferListDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IBankTransferService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class BankTransferControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IBankTransferService bankTransferService;

    @Test
    public void transferFromBankStatusOK() throws Exception
    {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setBankAccountId(20);
        bankTransferDto.setUserId(5);
        bankTransferDto.setAmount(new BigDecimal(250.45));

        when(bankTransferService.transferFromBank(bankTransferDto)).thenReturn(new BankTransferDto());

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

    @Test
    public void transferToBankStatusOK() throws Exception
    {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setBankAccountId(20);
        bankTransferDto.setUserId(5);
        bankTransferDto.setAmount(new BigDecimal(250.45));

        when(bankTransferService.transferToBank(bankTransferDto)).thenReturn(new BankTransferDto());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transferToBank").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankTransferDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void transferToBankWithException() throws Exception
    {
        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setBankAccountId(20);
        bankTransferDto.setUserId(5);
        bankTransferDto.setAmount(new BigDecimal(250.45));

        given(bankTransferService.transferToBank(bankTransferDto)).willThrow(new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transferToBank").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankTransferDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

    @Test
    void getBankTransferListForUser_WithException() throws Exception
    {
        given(bankTransferService.getAllTransferForUser(anyInt())).willThrow(
                new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/transfers").
                contentType(MediaType.APPLICATION_JSON).param("userId","");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

    @Test
    void getBankTransferListForUser_StatusOk() throws Exception
    {
        when(bankTransferService.getAllTransferForUser(anyInt())).thenReturn(new BankTransferListDto());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/transfers").
                contentType(MediaType.APPLICATION_JSON).param("userId", "1");

        mockMvc.perform(builder).
                andExpect(status().isOk());

    }
}
