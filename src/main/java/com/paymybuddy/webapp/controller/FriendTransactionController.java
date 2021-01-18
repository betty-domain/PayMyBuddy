package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFriendTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendTransactionController {
    private static final Logger logger = LogManager.getLogger(FriendshipController.class);

    @Autowired
    IFriendTransactionService transactionService;

    @PostMapping("/transaction")
    @JsonView(DtoJsonView.Public.class)
    public TransactionDto transferToFriend(@Validated @RequestBody IncomingTransactionDto incomingTransactionDto) throws FunctionalException {
        logger.info("Requête Post sur le endpoint transaction reçue");

        TransactionDto transactionDto = transactionService.transferToFriend(incomingTransactionDto);
        logger.info("Réponse transmise suite à requête Post sur le endpoint transaction");

        return transactionDto;
    }

}
