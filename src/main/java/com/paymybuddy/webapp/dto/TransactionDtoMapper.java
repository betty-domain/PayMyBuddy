package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import com.paymybuddy.webapp.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = { UserDtoMapper.class, FeeDtoMapper.class })
public interface TransactionDtoMapper {

    @Mappings({
            @Mapping(target = "amount", source = "transaction.amount"),
            @Mapping(target = "date", source = "transaction.date")
    })
    public TransactionDto mapFromTransaction(Transaction transaction, Fee fee);
}
