package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.TestsUtils;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IUserService;

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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IUserService userServiceMock;

    @Test
    void addUserValidTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setLastname("myLastName");
        userDto.setFirstname("myFirstName");
        userDto.setPassword("myPassword");
        userDto.setEmail("myEmail@gmail.com");

        when(userServiceMock.addUser(userDto)).thenReturn(userDto);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/user").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(userDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void addUserWithException() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setLastname("myLastName");
        userDto.setFirstname("myFirstName");
        userDto.setPassword("myPassword");
        userDto.setEmail("myEmail@gmail.com");
        when(userServiceMock.addUser(any(UserDto.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/user").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(userDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("user.add.error");
                });
    }

    @Test
    void updateUserValidTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setLastname("myLastName");
        userDto.setFirstname("myFirstName");
        userDto.setPassword("myPassword");
        userDto.setEmail("myEmail@gmail.com");

        when(userServiceMock.updateUser(userDto)).thenReturn(userDto);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/user").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(userDto));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    void updateUserWithException() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setLastname("myLastName");
        userDto.setFirstname("myFirstName");
        userDto.setPassword("myPassword");
        userDto.setEmail("myEmail@gmail.com");
        when(userServiceMock.updateUser(any(UserDto.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/user").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(userDto));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("user.update.error");
                });
    }

    @Test
    void getAllUsersValidTest() throws Exception {
        User user = new User();
        user.setLastname("myLastName");
        user.setFirstname("myFirstName");
        user.setPassword("myPassword");
        user.setEmail("myEmail@gmail.com");
        user.setBalance(new BigDecimal(50));

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userServiceMock.getAllUsers()).thenReturn(new ArrayList<>());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllUsersWithException() throws Exception {

        User user = new User();
        user.setLastname("myLastName");
        user.setFirstname("myFirstName");
        user.setPassword("myPassword");
        user.setEmail("myEmail@gmail.com");
        user.setBalance(new BigDecimal(50));

        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userServiceMock.getAllUsers()).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("users.get.error");
                });
    }
}
