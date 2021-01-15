package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.UserService;
import com.paymybuddy.webapp.utils.EncryptUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")
})
class UserIT {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EncryptUtils encryptUtils;

    @Test
    void addUserTest()
    {
        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname");
        userDtoTest.setLastname("lastname");
        userDtoTest.setPassword("password");
        userDtoTest.setEmail("email@gmail.com");

        UserDto userDtoCreated = userService.addUser(userDtoTest);

        assertThat(userDtoCreated).isNotNull();
    }

    @Test
    void updateUserTest()
    {
        User existingUser = userRepository.findUserByEmailIgnoreCase("updateUser@free.fr").get();
        String password = existingUser.getPassword();

        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname modified");
        userDtoTest.setLastname("lastname modified");
        userDtoTest.setPassword("password modified");
        userDtoTest.setEmail("updateUser@free.fr");
        userDtoTest.setBalance(null);

        UserDto userDto = userService.updateUser(userDtoTest);

        assertThat(userDto.getLastname()).isEqualTo(userDtoTest.getLastname());
        assertThat(userDto.getFirstname()).isEqualTo(userDtoTest.getFirstname());
        assertThat(userDto.getPassword()).isNotEqualTo(password);
        assertThat(userDto.getBalance()).isEqualTo(existingUser.getBalance());
    }

    @Test
    void getAllUsersTest()
    {
        List<UserDto> userList= userService.getAllUsers();
        assertThat(userList.stream().filter(userDto -> userDto.getEmail().equalsIgnoreCase("updateUser@free.fr")).findFirst()).isPresent();
        assertThat(userList.size()).isGreaterThanOrEqualTo(4);
    }
}
