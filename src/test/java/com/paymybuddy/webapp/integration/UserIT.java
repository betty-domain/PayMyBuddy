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
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:removeData.sql")
})
public class UserIT {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EncryptUtils encryptUtils;

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
        User existingUser = userRepository.findUserByEmailIgnoreCase("updateUser@free.fr").get();
        String password = existingUser.getPassword();

        UserDto userDtoTest = new UserDto();
        userDtoTest.setFirstname("firstname modified");
        userDtoTest.setLastname("lastname modified");
        userDtoTest.setPassword("password modified");
        userDtoTest.setEmail("updateUser@free.fr");

        User user = userService.updateUser(userDtoTest);

        assertThat(user.getId()).isEqualTo(existingUser.getId());
        assertThat(user.getLastname()).isEqualTo(userDtoTest.getLastname());
        assertThat(user.getFirstname()).isEqualTo(userDtoTest.getFirstname());
        assertThat(user.getPassword()).isNotEqualTo(password);
        assertThat(user.getBalance()).isEqualTo(existingUser.getBalance());
        assertThat(user.getBankAccountList()).isEqualTo(existingUser.getBankAccountList());
        assertThat(user.getBankTransferList()).isEqualTo(existingUser.getBankTransferList());
        assertThat(user.getFriendshipList()).isEqualTo(existingUser.getFriendshipList());
        assertThat(user.getAllTransaction()).isEqualTo(existingUser.getAllTransaction());

    }

    @Test
    public void getAllUsersTest()
    {
        List<User> userList= userService.getAllUsers();
        assertThat(userList.stream().filter(user -> user.getEmail().equalsIgnoreCase("updateUser@free.fr")).findFirst()).isPresent();
        assertThat(userList.size()).isGreaterThanOrEqualTo(4);
    }
}
