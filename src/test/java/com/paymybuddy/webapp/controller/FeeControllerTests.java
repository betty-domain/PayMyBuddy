package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class FeeControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IFeeService feeService;

    @Test
    public void getAllFeeForUser_Ok() throws Exception
    {

        when(feeService.getAllFeeForUser(anyInt())).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/fee").
                contentType(MediaType.APPLICATION_JSON).param("userId","5");

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void getAllFeeForUserWithException() throws Exception
    {

        given(feeService.getAllFeeForUser(anyInt())).willThrow(new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/fee").
                contentType(MediaType.APPLICATION_JSON).param("userId","5");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }
}
