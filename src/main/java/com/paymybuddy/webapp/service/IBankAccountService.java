package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.model.FunctionalException;

import java.util.List;

public interface IBankAccountService {
    /**
     * Ajout d'un compte bancaire
     * @param bankAccountDto
     * @return compte bancaire créé
     */
    BankAccountDto addBankAccount(BankAccountDto bankAccountDto) throws FunctionalException;

    /**
     * Désactivation d'un compte bancaire
     * @param bankAccountId id du compte à supprimer
     * @return nombre de comptes bancaires supprimés
     */
    boolean desactivateBankAccount(Integer bankAccountId) throws FunctionalException;

    /**
     * Récupère la liste des comptes bancaires actifs pour un user
     * @param userId id du user
     * @return Liste des comptes bancaire actifs, liste vide si aucun compte actif
     */
    List<BankAccountDto> getBankAccountListForUser(Integer userId) throws FunctionalException;
}
