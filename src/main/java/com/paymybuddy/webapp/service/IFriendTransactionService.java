package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;

public interface IFriendTransactionService {

    /**
     * Enregistrement d'une transaction d'argent vers un ami, avec application des taxes sur les transferts d'argent
     * @param incomingTransactionDto transaction à enregistrer
     * @return transaction réalisée reprenant le payeur, le bénéficiare et la taxe appliquée
     */
    TransactionDto transferToFriend(IncomingTransactionDto incomingTransactionDto);
}
