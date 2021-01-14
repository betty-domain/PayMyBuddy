package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.model.BankTransferOrder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BankTransferDto {

    @NotNull
    @JsonView(DtoJsonView.Private.class)
    private Integer userId;

    @NotNull
    @JsonView(DtoJsonView.Private.class)
    private Integer bankAccountId;

    @NotNull
    @JsonView(DtoJsonView.Public.class)
    private BigDecimal amount;

    @JsonView(DtoJsonView.Public.class)
    private BankTransferOrder transferOrder;

    public boolean isValid() {
        if (userId == null) {
            return false;
        }
        if (bankAccountId == null) {
            return false;
        }
        if (amount == null) {
            return false;
        }
        return true;
    }
}
