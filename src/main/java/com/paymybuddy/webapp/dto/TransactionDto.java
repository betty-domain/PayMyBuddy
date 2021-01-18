package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonView(DtoJsonView.Public.class)
public class TransactionDto {

    private String payerEmail;

    private String beneficiaryEmail;

    @JsonView(DtoJsonView.Private.class)
    private User payer;

    @JsonView(DtoJsonView.Private.class)
    private User beneficiary;

    private BigDecimal amount;

    private String description;

    private LocalDate date;

    private FeeDto fee;

    @JsonIgnore
    public String getPayerEmail() {
        return this.payer.getEmail();
    }

    @JsonIgnore
    public String getBeneficiaryEmail()
    {
        return this.beneficiary.getEmail();
    }
}
