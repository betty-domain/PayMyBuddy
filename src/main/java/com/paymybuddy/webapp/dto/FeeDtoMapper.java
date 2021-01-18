package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeeDtoMapper {

    public FeeDto mapFromFee(Fee fee);

}
