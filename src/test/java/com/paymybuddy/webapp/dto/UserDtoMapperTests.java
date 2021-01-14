package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDtoMapperTests {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @MockBean
    private EncryptUtils encryptUtils;

    @Test
    public void mapToUser_WithNullDto() {
        assertThat(userDtoMapper.mapToUser(null)).isNull();
    }

    @Test
    public void mapToUser_WithDtoNotNull() {
        UserDto userDto = new UserDto();
        userDto.setPassword("password");
        userDto.setLastname("lastname");
        userDto.setFirstname("firstame");
        userDto.setEmail("email@gmail.com");

        String encryptedPassword = "encryptedPassword";
        when(encryptUtils.encodePassword(userDto.getPassword())).thenReturn(encryptedPassword);

        User mappedUser = userDtoMapper.mapToUser(userDto);

        assertThat(mappedUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(mappedUser.getFirstname()).isEqualTo(userDto.getFirstname());
        assertThat(mappedUser.getLastname()).isEqualTo(userDto.getLastname());
        assertThat(mappedUser.getPassword()).isEqualTo(encryptedPassword);
    }

    @Test
    public void updateUserFromUserDto_ValidMapping() {

        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(40);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname modified");
        userDtoTest.setLastname("lastname modified");
        userDtoTest.setPassword("password modified");
        userDtoTest.setEmail("updateUser@free.fr");
        userDtoTest.setBalance(new BigDecimal(25));

        String encryptedPassword = "encryptedPassword";
        when(encryptUtils.encodePassword(userDtoTest.getPassword())).thenReturn(encryptedPassword);

        userDtoMapper.updateUserFromUserDto(userDtoTest,user);

        assertThat(user.getId()).isEqualTo(40);
        assertThat(user.getLastname()).isEqualTo(userDtoTest.getLastname());
        assertThat(user.getFirstname()).isEqualTo(userDtoTest.getFirstname());
        assertThat(user.getPassword()).isEqualTo(encryptedPassword);
        assertThat(user.getBalance()).isEqualTo(new BigDecimal(25));
        assertThat(user.getBankAccountList()).isNotNull();
        assertThat(user.getBankTransferList()).isNotNull();
        assertThat(user.getFriendshipList()).isNotNull();
        assertThat(user.getTransactionList()).isNotNull();
    }

    @Test
    public void updateUserFromUserDto_NullDto() {

        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(40);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        userDtoMapper.updateUserFromUserDto(null,user);

        assertThat(user.getId()).isEqualTo(40);
        assertThat(user.getLastname()).isEqualTo("lastname");
        assertThat(user.getFirstname()).isEqualTo("firstname");
        assertThat(user.getPassword()).isEqualTo("myPassword");
        assertThat(user.getBalance()).isEqualTo(new BigDecimal(250));
        assertThat(user.getBankAccountList()).isNotNull();
        assertThat(user.getBankTransferList()).isNotNull();
        assertThat(user.getFriendshipList()).isNotNull();
        assertThat(user.getTransactionList()).isNotNull();


    }

    @Test
    public void updateUserFromUserDto_NullDtoProperties() {

        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(40);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        UserDto userDto = new UserDto();

        userDtoMapper.updateUserFromUserDto(userDto,user);

        assertThat(user.getId()).isEqualTo(40);
        assertThat(user.getLastname()).isEqualTo("lastname");
        assertThat(user.getFirstname()).isEqualTo("firstname");
        assertThat(user.getPassword()).isEqualTo("myPassword");
        assertThat(user.getBalance()).isEqualTo(new BigDecimal(250));
        assertThat(user.getBankAccountList()).isNotNull();
        assertThat(user.getBankTransferList()).isNotNull();
        assertThat(user.getFriendshipList()).isNotNull();
        assertThat(user.getTransactionList()).isNotNull();


    }
    @Test
    public void mapFromUser_NullUser()
    {
        assertThat(userDtoMapper.mapFromUser(null)).isNull();
    }

    @Test
    public void mapFromUser_ValidMapping()
    {
        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(40);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        UserDto userDto = userDtoMapper.mapFromUser(user);
        assertThat(userDto.getBalance()).isEqualTo(user.getBalance());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(userDto.getLastname()).isEqualTo(user.getLastname());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
    }
}
