package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankTransferOrder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BankTransferDto {

    @NotNull
    private Integer userId;

    @NotNull
    private Integer bankAccountId;

    @NotNull
    private BigDecimal amount;

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
