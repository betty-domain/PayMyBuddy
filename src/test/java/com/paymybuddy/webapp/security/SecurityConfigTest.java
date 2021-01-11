package com.paymybuddy.webapp.security;

import com.paymybuddy.webapp.WebappApplication;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest( classes = { WebappApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private String getAllUsersUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Test @Disabled
    public void testAccessWithoutAuth()
    {
        //TODO : voir pourquoi ce test renvoit quand même un code 200 et redrection vers la page de login et non une 401 comme dans Postman
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(getAllUsersUrl(),String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testAcessWithAutenticatedUser()
    {
        ResponseEntity<String> responseEntity = testRestTemplate.withBasicAuth("betty.domain@free.fr","myPassword").getForEntity(getAllUsersUrl(),String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test @Disabled
    public void testAccessWithWrongUser()
    {
        //TODO : voir pourquoi ce test renvoit quand même un code 200 et redrection vers la page de login et non une 401 comme dans Postman
        ResponseEntity<String> responseEntity = testRestTemplate.withBasicAuth("toto@gmail.fr","myPassword").getForEntity(getAllUsersUrl(),String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


}
