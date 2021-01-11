package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountDtoMapper {


    public BankAccount mapToBankAccount(BankAccountDto bankAccountDto);

}
