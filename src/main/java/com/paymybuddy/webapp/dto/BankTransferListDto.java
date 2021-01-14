package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import lombok.Data;

import java.util.List;

@Data
@JsonView(DtoJsonView.Public.class)
public class BankTransferListDto {

    private UserDto userDto;

    private List<BankAccountDto> bankAccountDtoList;

}
