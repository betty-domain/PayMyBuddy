package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:removeData.sql")
})
public class UserIT {

    @Autowired
    private UserService userService;

    @Test
    public void addUserTest()
    {
        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname");
        userDtoTest.setLastname("lastname");
        userDtoTest.setPassword("password");
        userDtoTest.setEmail("email@gmail.com");

        User user = userService.addUser(userDtoTest);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void updateUserTest()
    {
        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname");
        userDtoTest.setLastname("lastname");
        userDtoTest.setPassword("password");
        userDtoTest.setEmail("updateUser@free.fr");

        User user = userService.updateUser(userDtoTest);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void getAllUsersTest()
    {
        List<User> userList= userService.getAllUsers();
        assertThat(userList.stream().filter(user -> user.getEmail().equalsIgnoreCase("updateUser@free.fr")).findFirst()).isPresent();
        assertThat(userList.size()).isGreaterThanOrEqualTo(4);
    }
}
