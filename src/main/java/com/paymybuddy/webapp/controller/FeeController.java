package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.DtoJsonView;
import com.paymybuddy.webapp.dto.FeeDto;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.service.IFeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeeController {

    private static final Logger logger = LogManager.getLogger(FeeController.class);

    @Autowired
    IFeeService feeService;

    @GetMapping("/fee")
    @JsonView(DtoJsonView.Public.class)
    public List<FeeDto> getAllFeeForUser(@Validated @RequestParam Integer userId) throws FunctionalException {
        logger.info("Requête Get sur le endpoint fee reçue");

        List<FeeDto> feeDtoList = feeService.getAllFeeForUser(userId);
        logger.info("Réponse transmise suite à requête get sur le endpoint fee");

        return feeDtoList;
    }
}
