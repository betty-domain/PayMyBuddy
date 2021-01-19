package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.model.FunctionalException;

import java.util.List;

public interface IFriendTransactionService {

    /**
     * Enregistrement d'une transaction d'argent vers un ami, avec application des taxes sur les transferts d'argent
     * @param incomingTransactionDto transaction à enregistrer
     * @return transaction réalisée reprenant le payeur, le bénéficiare et la taxe appliquée
     */
    TransactionDto transferToFriend(IncomingTransactionDto incomingTransactionDto) throws FunctionalException;

    /**
     * Récupère l'ensemble des transactions liées à l'utilisateur (vers des amis ou depuis des amis)
     * @param userId
     * @return
     */
    List<TransactionDto> getAllTransactionForUser(Integer userId) throws FunctionalException;
}
