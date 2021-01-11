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
import javax.swing.text.html.Option;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void updateUserWithNullDto()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(null);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    public void updateUserWithEmailNull() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);

        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDto);
                }
        );

        assertThat(exception.getMessage()).contains("Objet ou email null");
    }

    @Test
    public void updateNonExistingUser() {
        when(userRepositoryMock.findUserByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
                    userService.updateUser(userDtoTest);
                }
        );

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    public void updateNonValidUser() {
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
    public void updateUserOk()
    {
        User userToUpdate = new User();

        when(userRepositoryMock.findUserByEmailIgnoreCase(userDtoTest.getEmail())).thenReturn(Optional.of(userToUpdate));
        when(userRepositoryMock.save(any(User.class))).thenReturn(new User());

        User updateUser = userService.updateUser(userDtoTest);

        assertThat(updateUser).isNotNull();
        verify(userRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    public void getAllUsersTest()
    {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(15);
        userList.add(user);
        when(userRepositoryMock.findAll()).thenReturn(userList);

        assertThat(userService.getAllUsers()).contains(user);
    }
}
