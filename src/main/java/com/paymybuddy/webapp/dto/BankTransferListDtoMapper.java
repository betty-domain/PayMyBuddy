package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, BankAccountDtoMapper.class})
public interface BankTransferListDtoMapper {

    @Mappings({
            @Mapping(target = "userDto", source = "user"),
            @Mapping(target = "bankAccountDtoList",source="user.bankAccountList")
    })
    BankTransferListDto mapToBankTransferListDto(User user);
}
