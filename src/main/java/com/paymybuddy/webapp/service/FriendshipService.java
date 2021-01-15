package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FriendshipService implements IFriendshipService{
    @Override
    public boolean desactivateFriendship(final Integer userId, final Integer friendUserId) {
        return false;
    }

    @Override
    public UserDto addFriend(final Integer userId, final Integer friendUserId) {
        return null;
    }

    @Override
    public FriendshipDTO getFriendForUser(final Integer userId) {
        return null;
    }
}
