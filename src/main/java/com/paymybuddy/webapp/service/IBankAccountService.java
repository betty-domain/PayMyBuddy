package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;

public interface IBankAccountService {
    /**
     * Ajout d'un compte bancaire
     * @param bankAccountDto
     * @return compte bancaire créé
     */
    BankAccountDto addBankAccount(BankAccountDto bankAccountDto);

    /**
     * suppression d'un compte bancaire
     * @param bankAccountId id du compte à supprimer
     * @return nombre de comptes bancaires supprimés
     */
    boolean deleteBankAccount(Integer bankAccountId);

}
