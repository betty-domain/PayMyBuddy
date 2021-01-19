package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeeDtoMapper {

    public FeeDto mapFromFee(Fee fee);

    public List<FeeDto> mapFromFeeList(List<Fee> fee);

}
