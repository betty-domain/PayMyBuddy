package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.FunctionalException;

public interface IFriendshipService {
    /**
     * Désactive un lien d'amitié entre 2 users
     * @param userId userId pour lequel on souhaite désactiver un lien avec un ami
     * @param friendUserId userId de l'ami qui sera désactivé pour le user
     * @return true si le lien a été désactivé, false sinon
     */
    boolean desactivateFriendship(Integer userId, Integer friendUserId) throws FunctionalException;

    /**
     * Ajoute un ami pour un utilisateur
     * @param userId id de l'utilisateur pour lequel on souhaite ajouter un ami
     * @param friendUserId id de l'ami à ajouter
     * @return ami ajouté sur l'utilisateur
     */
    UserDto addFriend(Integer userId, Integer friendUserId) throws FunctionalException;

    /**
     * Récupère la liste des amis actifs d'un utilisateur
     * @param userId id de l'utilisateur
     * @return object représentant l'utilisateur et ses amis actifs
     */
    FriendshipDTO getFriendForUser(Integer userId) throws FunctionalException;

}
