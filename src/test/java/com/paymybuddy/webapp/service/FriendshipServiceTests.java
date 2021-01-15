package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.Friendship;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.FriendshipRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendshipServiceTests {

    @Autowired
    private IFriendshipService friendshipService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FriendshipRepository friendshipRepositoryMock;

    @Test
    void addUser_WithNullId() {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(null, null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void addFriendship_NonExistingUser() {

        Integer userId = 5;
        Integer friendUserId=15;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");

    }

    @Test
    void addFriendship_NonExistingFriend() {

        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        Integer friendUserId=15;
        User friend = new User();
        friend.setId(friendUserId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Ami : Utilisateur inexistant");
    }

    @Test
    void addFriendship_AlreadyExist()
    {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        Integer friendUserId=15;
        User friend = new User();
        friend.setId(friendUserId);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setAmi(friend);
        friendship.setActif(true);

        user.getFriendshipList().add(friendship);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Cette association d'amitié existe déjà");
    }

    @Test
    void addFriendship_NonExistingFriendship() {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        Integer friendUserId=15;
        User friend = new User();
        friend.setId(friendUserId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.of(friend));

        when(friendshipRepositoryMock.save(any())).thenReturn(new Friendship());

        UserDto userDto = friendshipService.addFriend(userId,friendUserId);
        assertThat(userDto).isNotNull();
    }

    @Test
    void addFriendship_ExistingUnactiveFriendship() {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        Integer friendUserId=15;
        User friend = new User();
        friend.setId(friendUserId);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setAmi(friend);
        friendship.setActif(false);

        user.getFriendshipList().add(friendship);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.of(friend));

        when(friendshipRepositoryMock.save(any())).thenReturn(new Friendship());

        UserDto userDto = friendshipService.addFriend(userId,friendUserId);
        assertThat(userDto).isNotNull();
    }
}
