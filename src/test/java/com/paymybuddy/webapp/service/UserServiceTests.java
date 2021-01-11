package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.assertj.core.api.BigDecimalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.jws.soap.SOAPBinding;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private UserService userService;

    private UserDto userDtoTest;

    @BeforeEach
    private void setUpEachTest() {

        userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname");
        userDtoTest.setLastname("lastname");
        userDtoTest.setPassword("password");
        userDtoTest.setEmail("email@gmail.com");
    }

    @Test
    public void addUserWithNullDto() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(null);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    public void addUserWithEmailNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDto);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    public void addExistingUser() {
        Optional<User> optionalUser = Optional.of(new User());
        when(userRepositoryMock.findUserByEmailIgnoreCase(anyString())).thenReturn(optionalUser);
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Utilisateur déjà existant");
    }

    @Test
    public void addNonValidUser() {
        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.empty());
        userDtoTest.setLastname("");

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    public void addUserOk()
    {
        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(any(User.class))).thenReturn(new User());

        User createdUser = userService.addUser(userDtoTest);

        assertThat(createdUser).isNotNull();
        verify(userRepositoryMock, Mockito.times(1)).save(any());
    }
}
