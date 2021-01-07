package com.paymybuddy.webapp.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Integer id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal percentage100;

    @NotNull
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name="transaction_id", nullable = false)
    private Transaction transaction;

}
