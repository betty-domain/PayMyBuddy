package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.service.IFriendshipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")
})
public class FriendshipIT {

    @Autowired
    IFriendshipService friendshipService;


    @Test
    void addFriendshipTest()
    {
        Integer userId=2;
        String friendEmail="hermione.granger@gmail.com";

        UserDto affectedFriend = friendshipService.addFriend(userId,friendEmail);

        assertThat(affectedFriend).isNotNull();

    }

    @Test
    void deleteFriendShip()
    {
        Integer userId=1;
        Integer friendId=3;

        assertThat(friendshipService.desactivateFriendship(userId,friendId)).isTrue();

    }

    @Test
    void getFriendsForUser()
    {
        Integer userId = 3;
        FriendshipDTO friendshipDTO = friendshipService.getFriendForUser(userId);

        assertThat(friendshipDTO.getFriends().size()).isEqualTo(2);
        assertThat(friendshipDTO.getFriends().stream().allMatch(userDto -> userDto.isValid())).isTrue();
    }
}
