package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests {

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
    void addUserWithNullDto() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(null);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    void addUserWithEmailNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDto);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    void addUserWithDBException() {

        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.empty());

        given(userRepositoryMock.save(any())).willAnswer(invocation -> {
            throw new Exception();
        });

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Erreur lors de l'enregistrement");
    }

    @Test
    void addExistingUser() {
        Optional<User> optionalUser = Optional.of(new User());
        when(userRepositoryMock.findUserByEmailIgnoreCase(anyString())).thenReturn(optionalUser);
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Utilisateur déjà existant");
    }

    @Test
    void addNonValidUser() {
        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.empty());
        userDtoTest.setLastname("");

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.addUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void addUserOk()
    {
        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(any(User.class))).thenReturn(new User());

        UserDto createdUser = userService.addUser(userDtoTest);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getBalance()).isEqualTo(BigDecimal.ZERO);
        verify(userRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    void updateUserWithNullDto()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(null);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    void updateUserWithEmailNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDto);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    void updateNonExistingUser() {
        when(userRepositoryMock.findUserByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void updateNonValidUser() {
        User userToUpdate = new User();

        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.of(userToUpdate));
        userDtoTest.setFirstname("");

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void updateUserWithDBException() {
        User userToUpdate = new User();

        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.of(userToUpdate));

        given(userRepositoryMock.save(any())).willAnswer(invocation -> {
            throw new Exception();
        });

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDtoTest );
                }
        );

        assertThat(exception.getMessage()).contains("Erreur lors de l'enregistrement");
    }

    @Test
    void updateUserOk()
    {
        User userToUpdate = new User();

        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.of(userToUpdate));
        when(userRepositoryMock.save(any(User.class))).thenReturn(new User());

        UserDto updateUser = userService.updateUser(userDtoTest);

        assertThat(updateUser).isNotNull();
        verify(userRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    void getAllUsersTest()
    {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(15);
        user.setEmail("myEmail@free.fr");
        userList.add(user);
        when(userRepositoryMock.findAll()).thenReturn(userList);

        assertThat(userService.getAllUsers().stream().filter(userDto -> userDto.getEmail().equalsIgnoreCase(user.getEmail())).findFirst()).isPresent();
    }

    @Test
    void getAllUsersTestWithDbException()
    {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(15);
        user.setEmail("myEmail@free.fr");
        userList.add(user);

        given(userRepositoryMock.findAll()).willAnswer(invocation -> {
            throw new Exception();
        });

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.getAllUsers();
                }
        );

    }
}
