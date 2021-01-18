package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { UserDtoMapper.class, FeeDtoMapper.class })
public interface TransactionDtoMapper {

    @Mappings({
            @Mapping(target = "amount", source = "fee.transaction.amount"),
            @Mapping(target = "date", source = "fee.transaction.date"),
            @Mapping(target = "payer", source = "fee.transaction.payer"),
            @Mapping(target = "beneficiary", source = "fee.transaction.beneficiary"),
            @Mapping(target = "description", source = "fee.transaction.description"),
            @Mapping(target = "fee", source = "fee")
    })
    public TransactionDto mapFromFee(Fee fee);
}
