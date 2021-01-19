package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FriendshipDTO;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.dto.UserDtoMapper;
import com.paymybuddy.webapp.model.Friendship;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.FriendshipRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendshipService implements IFriendshipService {
    private static final Logger logger = LogManager.getLogger(FriendshipService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Override
    public boolean desactivateFriendship(final Integer userId, final Integer friendUserId) throws FunctionalException {
        String errorKey = "friendship.delete.error : ";

        Friendship friendship = prepareFriendShipFromFriendId(userId, friendUserId, errorKey);

        Optional<Friendship> existingFriendship = friendship.getUser().getFriendshipList().stream().filter(
                friendshipIterator -> friendshipIterator.getUser().getId().equals(userId) && friendshipIterator.getAmi().getId().equals(friendUserId)
        ).findFirst();

        if (existingFriendship.isPresent() && existingFriendship.get().isActif()) {
            try {
                friendship.setActif(false);
                friendshipRepository.save(friendship);
                return true;
            } catch (Exception exception) {
                logger.error(errorKey + " erreur lors de l'enregsitrement : " + exception.getStackTrace());
                throw new FunctionalException(errorKey + "Erreur lors de l'enregistrement");
            }
        } else {
            logger.info(errorKey + "Cette association d'amitié n'existe pas");
            throw new FunctionalException(errorKey + "Cette association d'amitié n'existe pas");
        }
    }

    @Override
    public UserDto addFriend(final Integer userId, final String friendUserEmail) throws FunctionalException {
        String errorKey = "friendship.add.error : ";

        Friendship friendship = prepareFriendShipFromFriendEmail(userId, friendUserEmail, errorKey);

        Optional<Friendship> existingFriendship = friendship.getUser().getFriendshipList().stream().filter(
                friendshipIterator -> friendshipIterator.getUser().getId().equals(userId) && friendshipIterator.getAmi().getId().equals(friendship.getAmi().getId())
        ).findFirst();

        if (existingFriendship.isPresent() && existingFriendship.get().isActif()) {//si le lien d'amitié existe déjà --> erreur
            logger.info(errorKey + "Cette association d'amitié existe déjà");
            throw new FunctionalException(errorKey + "Cette association d'amitié existe déjà");
        } else {
            try {
                friendship.setActif(true);
                friendshipRepository.save(friendship);
                return userDtoMapper.mapFromUser(friendship.getAmi());
            } catch (Exception exception) {
                logger.error(errorKey + " erreur lors de l'enregsitrement : " + exception.getMessage());
                throw new FunctionalException(errorKey + "Erreur lors de l'enregistrement");
            }
        }

    }

    /**
     * Prépare l'entité et friendship et vérifie que tout est ok au préalable : données valides, utilisateur et ami existant
     *
     * @param userId          id de l'utilisateur
     * @param friendUserEmail email de l'ami
     * @param errorKey        message d'erreur pour les exceptions éventuelles
     * @return Lien d'amitié contenant l'utilisateur et l'ami
     * @throws FunctionalException exception métiers liées à l'absence de données ou d'utilisateurs
     */
    private Friendship prepareFriendShipFromFriendEmail(Integer userId, String friendUserEmail, String errorKey) throws FunctionalException {
        if (userId != null && friendUserEmail != null && !friendUserEmail.isEmpty()) {
            Optional<User> existingFriend = userRepository.findUserByEmailIgnoreCase(friendUserEmail);
            if (existingFriend.isPresent()) {//si l'ami existe
                return prepareFriendShip(userId, existingFriend.get(), errorKey);
            } else {
                logger.info("Ami : Utilisateur inexistant");
                throw new FunctionalException(errorKey + "Ami : Utilisateur inexistant");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }

    }

    /**
     * Prépare l'entité et friendship et vérifie que tout est ok au préalable : données valides, utilisateur et ami existant
     *
     * @param userId       id de l'utilisateur
     * @param friendUserId identifiant de l'ami
     * @param errorKey     message d'erreur pour les exceptions éventuelles
     * @return Lien d'amitié contenant l'utilisateur et l'ami
     * @throws FunctionalException exception métiers liées à l'absence de données ou d'utilisateurs
     */
    private Friendship prepareFriendShipFromFriendId(Integer userId, Integer friendUserId, String errorKey) throws FunctionalException {
        if (userId != null && friendUserId != null) {
            Optional<User> existingFriend = userRepository.findById(friendUserId);
            if (existingFriend.isPresent()) {//si l'ami existe
                return prepareFriendShip(userId, existingFriend.get(), errorKey);
            } else {
                logger.info("Ami : Utilisateur inexistant");
                throw new FunctionalException(errorKey + "Ami : Utilisateur inexistant");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }

    /**
     * Prépare l'entité et friendship et vérifie que tout est ok au préalable : données valides, utilisateur et ami existant
     *
     * @param userId   id de l'utilisateur
     * @param friend   ami
     * @param errorKey message d'erreur pour les exceptions éventuelles
     * @return Lien d'amitié contenant l'utilisateur et l'ami
     * @throws FunctionalException exception métiers liées à l'absence de données ou d'utilisateurs
     */
    private Friendship prepareFriendShip(Integer userId, User friend, String errorKey) throws FunctionalException {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {//si l'utilisateur existe
            if (friend.getId() == userId) {
                logger.info(errorKey + "Données incorrectes : id user et id ami identiques");
                throw new FunctionalException(errorKey + "Données incorrectes");
            } else {
                Friendship friendship = new Friendship();
                friendship.setUser(existingUser.get());
                friendship.setAmi(friend);
                return friendship;
            }
        } else {
            logger.info("Ami : Utilisateur inexistant");
            throw new FunctionalException(errorKey + "Ami : Utilisateur inexistant");
        }
    }

    @Override
    public FriendshipDTO getFriendForUser(final Integer userId) throws FunctionalException {
        String errorKey = "friendship.getForUser.error : ";
        if (userId != null) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {

                FriendshipDTO friendshipDTO = new FriendshipDTO();
                friendshipDTO.setUser(userDtoMapper.mapFromUser(existingUser.get()));

                //on récupère la liste des relations d'amitié actives
                List<Friendship> friendshipActiveList = existingUser.get().getFriendshipList().stream().filter(
                        Friendship::isActif).collect(Collectors.toList());

                if (friendshipActiveList != null && !friendshipActiveList.isEmpty()) {
                    //on extrait la liste des amis de la liste des amitiés actives
                    List<User> friendsList = friendshipActiveList.stream().map(Friendship::getAmi).distinct().collect(Collectors.toList());
                    friendshipDTO.setFriends(userDtoMapper.mapFromUserList(friendsList));
                }
                return friendshipDTO;
            } else {
                logger.info(errorKey + "Utilisateur inexistant");
                throw new FunctionalException(errorKey + "Utilisateur inexistant");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }
}
