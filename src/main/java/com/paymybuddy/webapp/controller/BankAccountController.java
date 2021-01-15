package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IBankAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountController {
    private static final Logger logger = LogManager.getLogger(BankAccountController.class);

    @Autowired
    private IBankAccountService bankAccountService;

    @PostMapping("/bankAccount")
    @JsonView(DtoJsonView.Public.class)
    /**
     * Ajoute un compte bancaire
     */
    public BankAccountDto addBankAccount(@Validated @RequestBody BankAccountDto bankAccountDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint bankAccount reçue");

        BankAccountDto createdBankAccountDto = bankAccountService.addBankAccount(bankAccountDto);
        logger.info("Réponse Post sur le endpoint bankAccount transmise");

        return createdBankAccountDto;
    }

    @DeleteMapping("/bankAccount")
    @JsonView(DtoJsonView.Public.class)
    /**
     * Suppression (désactivation) d'un compte bancaire
     */
    public String deleteBankAccount(@Validated @RequestParam Integer bankAccountId) throws FunctionalException {
        logger.info("Requête Delete sur le endpoint bankAccount reçue");

        Boolean deletedBankAccount = bankAccountService.desactivateBankAccount(bankAccountId);

        if (deletedBankAccount)
        {
            logger.info("Réponse Delete sur le endpoint bankAccount transmise");
            return ("Bank Account deleted");
        }
        else
        {
            throw new FunctionalException("An error occured during bank account deletion");
        }

    }

    @GetMapping("/bankAccounts")
    @JsonView(DtoJsonView.Public.class)
    /**
     * Récupère la liste des comptes bancaires actifs d'un utilisateur
     */
    public List<BankAccountDto> getBankAccountListForUser(@Validated @RequestParam Integer userId) throws FunctionalException
    {
        logger.info("Requête Get sur le endpoint BankAccounts reçue");
        List<BankAccountDto> bankAccountDtoList = bankAccountService.getBankAccountListForUser(userId);

        logger.info("Réponse sur le endpoint bankAccounts transmise");
        return bankAccountDtoList;
    }
}
