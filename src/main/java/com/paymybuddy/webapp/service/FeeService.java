package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FeeDto;
import com.paymybuddy.webapp.dto.FeeDtoMapper;
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
public class FeeService implements IFeeService{
    private static final Logger logger = LogManager.getLogger(FeeService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    @Override
    public List<FeeDto> getAllFeeForUser(final Integer userId) throws FunctionalException {
        String errorKey = "fee.get.error : ";
        if (userId==null)
        {
            logger.info(errorKey + "Données invalides");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
        else
        {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent())
            {
                return feeDtoMapper.mapFromFeeList(existingUser.get().getFeeList());
            }
            else
            {
                logger.info(errorKey + "Utilisateur inexistant");
                throw new FunctionalException(errorKey+"Utilisateur inexistant");
            }
        }
    }
}
