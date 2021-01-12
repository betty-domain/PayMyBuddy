package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IBankTransferService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankTransferController {

    private static final Logger logger = LogManager.getLogger(BankTransferController.class);

    @Autowired
    private IBankTransferService bankTransferService;

    @PostMapping("/transferFromBank")
    public BankTransfer transferFromBank(@Validated @RequestBody BankTransferDto bankTransferDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transferFromBank reçue");

        BankTransfer createdBankTransfer = bankTransferService.transferFromBank(bankTransferDto);
        logger.info("Réponse Post sur le endpoint transferFromBank transmise");

        return createdBankTransfer;
    }

    @PostMapping("/transferToBank")
    public BankTransfer transferToBank(@Validated @RequestBody BankTransferDto bankTransferDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transferToBank reçue");

        BankTransfer createdBankTransfer = bankTransferService.transferToBank(bankTransferDto);
        logger.info("Réponse Post sur le endpoint transferToBank transmise");

        return createdBankTransfer;
    }
}
