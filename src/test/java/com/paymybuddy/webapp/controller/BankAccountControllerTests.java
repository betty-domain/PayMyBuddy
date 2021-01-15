package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IBankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class BankAccountControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IBankAccountService bankAccountService;

    @Test
    public void addBankAccountStatusOK() throws Exception
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("iban");
        bankAccountDto.setUserId(25);

        when(bankAccountService.addBankAccount(bankAccountDto)).thenReturn(new BankAccount());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/bankAccount").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankAccountDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void addBankAccountWithException() throws Exception
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("iban");
        bankAccountDto.setUserId(25);

        given(bankAccountService.addBankAccount(bankAccountDto)).willThrow(new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/bankAccount").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(bankAccountDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

    @Test
    public void deleleBankAccountStatusOk() throws Exception
    {
        when(bankAccountService.deleteBankAccount(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/bankAccount").
                contentType(MediaType.APPLICATION_JSON).param("bankAccountId", "1");

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void deleteBankAccountBadRequest() throws Exception
    {
        given(bankAccountService.deleteBankAccount(anyInt())).willThrow(
                new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/bankAccount").
                contentType(MediaType.APPLICATION_JSON).param("bankAccountId","1");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

}
