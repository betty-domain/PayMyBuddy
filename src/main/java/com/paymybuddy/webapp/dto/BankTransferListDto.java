package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
@JsonView(DtoJsonView.Public.class)
public class BankTransferListDto {

    @JsonProperty("user")
    private UserDto userDto;

    @JsonProperty("bankAccountList")
    private List<BankAccountDto> bankAccountDtoList;

}
