package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.utils.DateUtils;

import org.junit.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class BankTransferDtoMapper {

    @Mappings({
            @Mapping(target="id",ignore = true),
            @Mapping(target="user", source="bankAccount.user"),
            @Mapping(target="bankAccount", source="bankAccount")

    })
    public abstract BankTransfer mapToBankTransfer(BankTransferDto bankTransferDto, BankAccount bankAccount);

    @AfterMapping
    protected void setDateOfTransfer(@MappingTarget BankTransfer bankTransfer) {
        bankTransfer.setDate(dateUtils.getNowLocalDate());
    }

    @Autowired
    DateUtils dateUtils;


}
