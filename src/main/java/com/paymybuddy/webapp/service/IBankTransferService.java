package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.dto.BankTransferListDto;
import com.paymybuddy.webapp.model.FunctionalException;


public interface IBankTransferService {
    /**
     * Transfer cash from bankAccount to PayMyBuddy balance
     * @param bankTransferDto object to define transfer
     * @return bankTransfer created
     * @throws FunctionalException exception if data are not valid for cash transfer
     */
    BankTransferDto transferFromBank(BankTransferDto bankTransferDto) throws FunctionalException;

    /**
     * Transfer cash from PayMyBuddy to bankAccount
     * @param bankTransferDto object to define transfer
     * @return bankTransfer created
     * @throws FunctionalException exception if data are not valid for cash transfer
     */
    BankTransferDto transferToBank(BankTransferDto bankTransferDto) throws FunctionalException;

    /**
     * Get all transfer for a defined user
     * @param userId id of defined user
     * @return Liste des transferts par utilisateur et compte, structuré dans un objet BankTransferListDto
     * @throws FunctionalException exception if data are not valid
     */
    BankTransferListDto getAllTransferForUser(Integer userId) throws FunctionalException;
}
