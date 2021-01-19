package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FriendshipDTO;
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
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        String friendEmail = "monemail@gmail.com";
        User friend = new User();
        friend.setEmail(friendEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findUserByEmailIgnoreCase(friendEmail)).thenReturn(Optional.of(friend));
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendEmail);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");

    }

    @Test
    void addFriendship_NonExistingFriend() {

        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        String friendEmail = "monemail@gmail.com";
        User friend = new User();
        friend.setEmail(friendEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmailIgnoreCase(friendEmail)).thenReturn(Optional.empty());
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendEmail);
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

        String friendEmail = "monemail@gmail.com";
        User friend = new User();
        friend.setEmail(friendEmail);
        friend .setId(15);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setAmi(friend);
        friendship.setActif(true);

        user.getFriendshipList().add(friendship);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmailIgnoreCase(friendEmail)).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.addFriend(userId, friendEmail);
        });

        assertThat(exception.getMessage()).contains("Cette association d'amitié existe déjà");
    }

    @Test
    void addFriendship_NonExistingFriendship() {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        String friendEmail = "monemail@gmail.com";
        User friend = new User();
        friend.setEmail(friendEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmailIgnoreCase(friendEmail)).thenReturn(Optional.of(friend));

        when(friendshipRepositoryMock.save(any())).thenReturn(new Friendship());

        UserDto userDto = friendshipService.addFriend(userId,friendEmail);
        assertThat(userDto).isNotNull();
    }

    @Test
    void addFriendship_ExistingUnactiveFriendship() {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        String friendEmail = "monemail@gmail.com";
        User friend = new User();
        friend.setEmail(friendEmail);
        friend.setId(15);


        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setAmi(friend);
        friendship.setActif(false);

        user.getFriendshipList().add(friendship);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmailIgnoreCase(friendEmail)).thenReturn(Optional.of(friend));

        when(friendshipRepositoryMock.save(any())).thenReturn(new Friendship());

        UserDto userDto = friendshipService.addFriend(userId,friendEmail);
        assertThat(userDto).isNotNull();
    }

    @Test
    void desactivateFriendship_WithNullId()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.desactivateFriendship(null, null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void desactivateFriendship_NonExistingUser()
    {
        Integer userId = 5;
        Integer friendUserId=15;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.desactivateFriendship(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void desactivateFriendship_NonExistingFriend()
    {
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
            friendshipService.desactivateFriendship(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Ami : Utilisateur inexistant");

    }

    @Test
    void desactivateFriendship_FriendshipDoesNotExist()
    {
        Integer userId = 5;
        User user = new User();
        user.setId(userId);
        user.setFriendshipList(new ArrayList<>());

        Integer friendUserId=15;
        User friend = new User();
        friend.setId(friendUserId);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.desactivateFriendship(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Cette association d'amitié n'existe pas");
    }

    @Test
    void desactivateFriendship_FriendshipAlreadyUnactive()
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
        friendship.setActif(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendUserId)).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.desactivateFriendship(userId, friendUserId);
        });

        assertThat(exception.getMessage()).contains("Cette association d'amitié n'existe pas");
    }

    @Test
    void desactivateFriendshipOK_WithActiveFriendship(){
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

        assertThat(friendshipService.desactivateFriendship(userId, friendUserId)).isTrue();
    }

    @Test
    void getFriendForUser_WithNullID()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.getFriendForUser(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");

    }
    @Test
    void getFriendForUser_NonExistingUser(){
        Integer userId = 5;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            friendshipService.getFriendForUser(userId);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");

    }

    @Test
    void getFriendForUser_OK(){
        Integer userId = 5;
        User user = new User();
        user.setId(5);
        user.setFirstname("firstname");
        user.setLastname("lastname");

        User friend1 = new User();
        friend1.setId(42);
        friend1.setLastname("lastname_Friend1");
        friend1.setFirstname("firstname_Friend1");
        Friendship fship1 = new Friendship();
        fship1.setUser(user);
        fship1.setAmi(friend1);
        fship1.setActif(true);
        user.getFriendshipList().add(fship1);

        User friend2 = new User();
        friend2.setId(48);
        friend2.setLastname("lastname_Friend2");
        friend2.setFirstname("firstname_Friend2");
        Friendship fship2 = new Friendship();
        fship2.setUser(user);
        fship2.setAmi(friend2);
        fship2.setActif(false);
        user.getFriendshipList().add(fship2);

        User friend3 = new User();
        friend3.setId(45);
        friend3.setLastname("lastname_Friend3");
        friend3.setFirstname("firstname_Friend3");
        Friendship fship3 = new Friendship();
        fship3.setUser(user);
        fship3.setAmi(friend3);
        fship3.setActif(true);
        user.getFriendshipList().add(fship3);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        FriendshipDTO friendshipDTO = friendshipService.getFriendForUser(userId);

        assertThat(friendshipDTO.getUser().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(friendshipDTO.getFriends().size()).isEqualTo(2);
        assertThat(friendshipDTO.getFriends().stream().allMatch(userDto ->
                userDto.getFirstname().equalsIgnoreCase(friend1.getFirstname())
        || userDto.getFirstname().equalsIgnoreCase(friend3.getFirstname())));
    }


}
