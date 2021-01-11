package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;

import java.util.List;

public interface IUserService {
    User addUser(UserDto userDto) throws FunctionalException;

    User updateUser(UserDto userDto) throws FunctionalException;

    List<User> getAllUsers();

}
