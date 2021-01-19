package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = { FeeDtoMapper.class })
public interface TransactionDtoMapper {

    @Mappings({
            @Mapping(target = "amount", source = "fee.transaction.amount"),
            @Mapping(target = "date", source = "fee.transaction.date"),
            @Mapping(target = "payerEmail", source = "fee.transaction.payer.email"),
            @Mapping(target = "beneficiaryEmail", source = "fee.transaction.beneficiary.email"),
            @Mapping(target = "description", source = "fee.transaction.description"),
            @Mapping(target = "fee", source = "fee")
    })
    public TransactionDto mapFromFee(Fee fee);

    public List<TransactionDto> mapFromFeeList(List<Fee> feeList);
}
