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

}
