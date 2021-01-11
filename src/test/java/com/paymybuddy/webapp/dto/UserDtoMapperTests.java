package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDtoMapperTests {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @MockBean
    private EncryptUtils encryptUtils;

    @Test
    public void mapToUser_WithNullDto()
    {
        assertThat(userDtoMapper.mapToUser(null)).isNull();
    }

    @Test
    public void mapToUser_WithDtoNotNull()
    {
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


}
