package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @PostMapping("/user")
    @JsonView(DtoJsonView.Public.class)
    public UserDto addUser(@Validated @RequestBody UserDto userDto) throws FunctionalException
    {
        logger.info("Requête Post sur le endpoint user reçue");

        UserDto createdUser = userService.addUser(userDto);
        if (createdUser != null) {
            logger.info("Réponse Post sur le endpoint user transmise");
            return createdUser;
        } else {
            logger.error("Erreur lors de la requête Post sur le endpoint user ");
            throw new FunctionalException("user.add.error");
        }

    }

    @PutMapping("/user")
    @JsonView(DtoJsonView.Public.class)
    public UserDto updateUser(@Validated @RequestBody UserDto userDto)
    {
        logger.info("Requête Put sur le endpoint user reçue");

        UserDto updatedUser = userService.updateUser(userDto);
        if (updatedUser !=null)
        {
            logger.info("Réponse Put sur le endpoint user transmise");
            return  updatedUser;
        }
        else
        {
            logger.error("Erreur lors de la requête Put sur le endpoint user ");
            throw new FunctionalException("user.update.error");
        }
    }

    @GetMapping("/users")
    @JsonView(DtoJsonView.Public.class)
    public List<UserDto> getAllUsers()
    {
        logger.info("Requête Get sur le endpoint users reçue");

        List<UserDto> userList = userService.getAllUsers();
        if (userList !=null)
        {
            logger.info("Réponse Get sur le endpoint users transmise");
            return  userList;
        }
        else
        {
            logger.error("Erreur lors de la requête Get sur le endpoint user ");
            throw new FunctionalException("users.get.error");
        }
    }
}
