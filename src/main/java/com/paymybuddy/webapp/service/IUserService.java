package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.User;

import java.util.List;

public interface IUserService {
    User addUser(UserDto userDto);

    User updateUser(UserDto userDto);

    List<User> getAllUsers();

}
