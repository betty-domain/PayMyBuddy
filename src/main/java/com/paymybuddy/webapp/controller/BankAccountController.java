package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.UserDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.IBankAccountService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {
    private static final Logger logger = LogManager.getLogger(BankAccountController.class);

    private IBankAccountService bankAccountService;

    @PostMapping("/bankAccount")
    public BankAccount addBankAccount(@Validated @RequestBody BankAccountDto bankAccountDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint bankAccount reçue");

        BankAccount createdBankAccount = bankAccountService.addBankAccount(bankAccountDto);
        logger.info("Réponse Post sur le endpoint bankAccount transmise");
        return createdBankAccount;
    }

    @DeleteMapping("/bankAccount")
    public boolean deleteBankAccount(@Validated @RequestParam Integer bankAccountId) throws FunctionalException {
        logger.info("Requête Delete sur le endpoint bankAccount reçue");

        Boolean accountDeleted = bankAccountService.deleteBankAccount(bankAccountId);
        logger.info("Réponse Delete sur le endpoint bankAccount transmise");
        return accountDeleted;
    }

}
