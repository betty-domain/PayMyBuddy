package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFriendshipService;
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
class FriendshipControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IFriendshipService friendshipServiceMock;

    @Test
    void addUser_StatusOk() throws Exception
    {
        when(friendshipServiceMock.addFriend(anyInt(),anyInt())).thenReturn(new UserDto());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5").
                param("friendUserId","15");

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void addUser_BadRequest() throws Exception
    {
        given(friendshipServiceMock.addFriend(anyInt(),anyInt())).willThrow(
                new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5").
                param("friendUserId","15");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

    @Test
    void removeFriend_StatusOk() throws Exception
    {
        when(friendshipServiceMock.desactivateFriendship(anyInt(),anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5").
                param("friendUserId","15");

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void removeFriend_BadRequest() throws Exception
    {
        given(friendshipServiceMock.desactivateFriendship(anyInt(),anyInt())).willThrow(
                new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5").
                param("friendUserId","15");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }

    @Test
    void getFriendForUser_StatusOk() throws Exception
    {
        when(friendshipServiceMock.getFriendForUser(anyInt())).thenReturn(new FriendshipDTO());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5");

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void getFriendForUser_BadRequest() throws Exception
    {
        given(friendshipServiceMock.getFriendForUser(anyInt())).willThrow(
                new FunctionalException("Exception Message"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/friendship").
                contentType(MediaType.APPLICATION_JSON).
                param("userId","5");

        mockMvc.perform(builder).
                andExpect(status().isBadRequest());
    }
}
