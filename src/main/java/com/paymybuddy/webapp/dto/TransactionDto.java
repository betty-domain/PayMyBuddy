package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonView(DtoJsonView.Public.class)
public class TransactionDto {

    private String payerEmail;

    private String beneficiaryEmail;

    private BigDecimal amount;

    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private FeeDto fee;
}
