package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public User addUser(@Validated @RequestBody UserDto userDto)
    {
        logger.info("Requête Post sur le endpoint user reçue");

        User createdUser = userService.addUser(userDto);
        if (createdUser !=null)
        {
            logger.info("Réponse Post sur le endpoint user transmise");
            return  createdUser;
        }
        else
        {
            logger.error("Erreur lors de la requête Post sur le endpoint user ");
            throw new FunctionalException("user.add.error");
        }
    }
}
