package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.model.BankAccount;

public interface IBankAccountService {
    /**
     * Ajout d'un compte bancaire
     * @param bankAccountDto
     * @return compte bancaire créé
     */
    BankAccount addBankAccount(BankAccountDto bankAccountDto);

    /**
     * suppression d'un compte bancaire
     * @param bankAccountId id du compte à supprimer
     * @return nombre de comptes bancaires supprimés
     */
    boolean deleteBankAccount(Integer bankAccountId);

}
