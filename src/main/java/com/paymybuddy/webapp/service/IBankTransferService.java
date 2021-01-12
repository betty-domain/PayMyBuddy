package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.FunctionalException;

public interface IBankTransferService {
    BankTransfer transferFromBank(BankTransferDto bankTransferDto) throws FunctionalException;

    BankTransfer transferToBank(BankTransferDto bankTransferDto) throws FunctionalException;
}
