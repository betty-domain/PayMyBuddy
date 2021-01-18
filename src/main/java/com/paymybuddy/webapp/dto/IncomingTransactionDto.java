package com.paymybuddy.webapp.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class IncomingTransactionDto {
    @NotNull
    private Integer payerId;

    @NotNull
    private Integer beneficiaryId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    @Size(max = 200)
    private String description;

    /**
     * vérifie si les données de l'entité sont valides (not null)
     * @return true si cvalide, false sinon
     */
    public boolean isValid()
    {
        if (payerId ==null)
        {
            return false;
        }
        if (beneficiaryId == null) {
            return false;
        }
        if (amount == null || amount.equals(BigDecimal.ZERO)) {
            return false;
        }

        if (description == null || description.isEmpty())
        {
            return  false;
        }
        return true;
    }
}
