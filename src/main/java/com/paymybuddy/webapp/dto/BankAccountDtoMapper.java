package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses=BankTransferDtoMapper.class)
public interface BankAccountDtoMapper {
    @Mappings({
            @Mapping(target="id", source="bankAccountDto.id"),
            @Mapping(target="bankTransferList", ignore = true)

    })
    public BankAccount mapToBankAccount(BankAccountDto bankAccountDto,User user);

    @Mappings({
            @Mapping(target="id", source="bankAccount.id"),
            @Mapping(target="userId", source="bankAccount.user.id"),
            @Mapping(target="bankTransferDtoList",source="bankTransferList")
    })
    public BankAccountDto mapFromBankAccount(BankAccount bankAccount);
}
