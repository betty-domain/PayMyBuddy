package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;

import java.util.List;

public interface IUserService {
    UserDto addUser(UserDto userDto) throws FunctionalException;

    UserDto updateUser(UserDto userDto) throws FunctionalException;

    List<UserDto> getAllUsers() throws FunctionalException;

}
