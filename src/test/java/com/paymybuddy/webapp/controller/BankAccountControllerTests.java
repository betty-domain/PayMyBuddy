package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.model.UserPrincipal;
import com.paymybuddy.webapp.service.IBankAccountService;
import com.paymybuddy.webapp.service.MyUserDetailsService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BankAccountController.class)
@Disabled
public class BankAccountControllerTests {

    @MockBean
    private MyUserDetailsService userDetailsServiceMock;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IBankAccountService bankAccountService;

    private BankAccountController bankAccountController;

    @WithMockUser(value="betty.domain@free.fr", password = "myPassword")
    @Test
    public void addBankAccountStatusOK() throws Exception
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("iban");
        bankAccountDto.setUserId(25);

        UserDetails userDetails = new UserPrincipal(new User());

        when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(userDetails);

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
}
