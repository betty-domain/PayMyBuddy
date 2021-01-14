package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BankAccountDtoMapper {
    @Mappings({
            @Mapping(target="id", source="bankAccountDto.id")
    })
    public BankAccount mapToBankAccount(BankAccountDto bankAccountDto,User user);

    @Mappings({
            @Mapping(target="id", source="bankAccount.id"),
            @Mapping(target="userId", source="bankAccount.user.id")
    })
    public BankAccountDto mapFromBankAccount(BankAccount bankAccount);
}
