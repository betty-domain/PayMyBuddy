package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.WebappApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebappApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigTest {

    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private String getAllUsersUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void testAccessWithoutAuth() throws Exception{

            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users").
                    contentType(MediaType.APPLICATION_JSON);

            mockMvc.perform(builder).
                    andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "harry.potter@gmail.com", password = "potter")
    public void testAccessWithAuthenticatedUser() throws Exception{

            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users").
                    contentType(MediaType.APPLICATION_JSON);

            mockMvc.perform(builder).
                    andExpect(status().isOk());
    }
}
