package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.model.BankTransferOrder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @JsonView(DtoJsonView.Public.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

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
