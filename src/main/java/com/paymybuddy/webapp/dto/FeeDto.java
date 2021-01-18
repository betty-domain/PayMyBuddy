package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonView(DtoJsonView.Public.class)
public class FeeDto {

    private BigDecimal amount;

    private BigDecimal percentage100;

    private LocalDate date;
}
