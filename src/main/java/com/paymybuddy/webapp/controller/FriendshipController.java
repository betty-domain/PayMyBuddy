package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFriendshipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendshipController {

    private static final Logger logger = LogManager.getLogger(FriendshipController.class);

    @Autowired
    IFriendshipService friendshipService;

    @PostMapping("/friendship")
    @JsonView(DtoJsonView.Public.class)
    public UserDto addFriend(@Validated @RequestParam Integer userId, @RequestParam Integer friendUserId) throws Exception
    {
        logger.info("Requête Post reçue sur le endpoint friendship");
        UserDto userDto = friendshipService.addFriend(userId,friendUserId);

        logger.info("Réponse suite à un post sur le endpoint friendship transmise");
        return userDto;
    }

    @DeleteMapping("/friendship")
    public Integer removeFriend(@Validated @RequestParam Integer userId, @RequestParam Integer friendUserId) throws Exception
    {
        logger.info("Requête Delete reçue sur le endpoint friendship");

        boolean isUserDesactivated = friendshipService.desactivateFriendship(userId,friendUserId);

        if (isUserDesactivated)
        {
            logger.info("Réponse suite à un delete sur le endpoint friendship transmise");
            return 1;
        }
        else
        {
            throw new FunctionalException("Erreur lors de la suppression d'un ami");
        }
    }

    @GetMapping("/friendship")
    @JsonView(DtoJsonView.Public.class)
    public FriendshipDTO getFriendForUser(@Validated @RequestParam Integer userId)
    {
        logger.info("Requête Get reçue sur le endpoint friendship");

        FriendshipDTO friendshipDTO = friendshipService.getFriendForUser(userId);

        logger.info("Réponse suite à un get sur le endpoint friendship transmise");
        return friendshipDTO;
    }

}
