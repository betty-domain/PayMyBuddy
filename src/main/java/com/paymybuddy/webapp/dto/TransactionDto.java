package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonView(DtoJsonView.Public.class)
public class TransactionDto {

    private String payerEmail;

    private String beneficiaryEmail;

    private BigDecimal amount;

    private String description;

    private LocalDate date;

    private FeeDto fee;
}
