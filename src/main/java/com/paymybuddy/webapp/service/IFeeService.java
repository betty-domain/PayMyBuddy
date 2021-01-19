package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FeeDto;
import com.paymybuddy.webapp.model.FunctionalException;

import java.util.List;

public interface IFeeService {
    /**
     * Récupère l'ensemble des taxes appliquées sur les transactions pour un utilisateur donné
     * @param userId utilisateur pour lequel on souhaite récupérer les taxes
     * @return listes des taxes, la liste peut être vide
     * @throws FunctionalException exception fonctionnelle pouvant être levée
     */
    List<FeeDto> getAllFeeForUser(Integer userId) throws FunctionalException;
}
