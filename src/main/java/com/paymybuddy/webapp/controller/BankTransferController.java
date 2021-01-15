package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.dto.BankTransferListDto;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IBankTransferService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BankTransferController {

    private static final Logger logger = LogManager.getLogger(BankTransferController.class);

    @Autowired
    private IBankTransferService bankTransferService;

    @PostMapping("/transferFromBank")
    public BankTransferDto transferFromBank(@Validated @RequestBody BankTransferDto bankTransferDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transferFromBank reçue");

        BankTransferDto createdBankTransferDto = bankTransferService.transferFromBank(bankTransferDto);
        logger.info("Réponse Post sur le endpoint transferFromBank transmise");

        return createdBankTransferDto;
    }

    @PostMapping("/transferToBank")
    public BankTransferDto transferToBank(@Validated @RequestBody BankTransferDto bankTransferDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transferToBank reçue");

        BankTransferDto createdBankTransferDto = bankTransferService.transferToBank(bankTransferDto);
        logger.info("Réponse Post sur le endpoint transferToBank transmise");

        return createdBankTransferDto;
    }

    @GetMapping("/transfers")
    @JsonView(DtoJsonView.Protected.class)
    public BankTransferListDto getAllTransfersForUser(@Validated @RequestParam Integer userId) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transfers reçue");

        BankTransferListDto bankTransferListDto = bankTransferService.getAllTransferForUser(userId);
        logger.info("Réponse Post sur le endpoint transfers transmise");

        return bankTransferListDto;
    }
}
