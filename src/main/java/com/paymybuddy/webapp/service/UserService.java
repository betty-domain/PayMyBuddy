package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.dto.UserDtoMapper;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDtoMapper userDtoMapper;

    /**
     * Ajout d'un utilisateur
     * @param userDto objet à ajouter
     * @return Objet Utilisateur créé
     */
    @Override
    public User addUser(final UserDto userDto) throws FunctionalException {

        String errorKey = "add.user.error : ";
        if (userDto!=null && userDto.getEmail()!=null)
        {
            logger.info("Recherche d'un utilisateur par son email avant de créer un nouvel utilisateur");
            Optional<User> existingUser = userRepository.findUserByEmailIgnoreCase(userDto.getEmail());

            if (existingUser.isPresent())
            {
                logger.error("Impossible d'ajouter un utilisateur déjà existant : " + userDto.getEmail());
                throw new FunctionalException(errorKey +  "Utilisateur déjà existant");
            }
            else
            {
                if (userDto.isValid()) {
                    User createdUser = userRepository.save(userDtoMapper.mapToUser(userDto));
                    logger.info("Ajout de l'utilisateur réussi");
                    return createdUser;
                }
                else
                {
                    logger.error("Données de l'entité userDto incorrectes, ajout impossible");
                    throw new FunctionalException(errorKey + "Données incorrectes");
                }
            }
        }
        throw new FunctionalException(errorKey + "Objet ou email null ");
    }

    @Override
    public User updateUser(final UserDto userDto) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

}
