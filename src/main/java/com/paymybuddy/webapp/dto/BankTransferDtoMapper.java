package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.utils.DateUtils;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BankTransferDtoMapper {

    @Mappings({
            @Mapping(target="id",ignore = true),
            @Mapping(target="user", source="bankAccount.user"),
            @Mapping(target="bankAccount", source="bankAccount"),
            @Mapping(target="date", ignore = true)
    })
    public abstract BankTransfer mapToBankTransfer(BankTransferDto bankTransferDto, BankAccount bankAccount);

    @AfterMapping
    protected void setDateOfTransfer(@MappingTarget BankTransfer bankTransfer) {
        bankTransfer.setDate(dateUtils.getNowLocalDateTime());
    }

    @Autowired
    DateUtils dateUtils;

    @Mappings({
            @Mapping(target="userId", source="bankTransfer.user.id"),
            @Mapping(target="bankAccountId", source="bankTransfer.bankAccount.id")

    })
    public abstract BankTransferDto mapFromBankTransfer(BankTransfer bankTransfer);

    public abstract List<BankTransferDto> mapFromBankTransferList(List<BankTransfer> bankTransferList);
}
