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
@Table(name= "transaction")
public class Transaction {

    @Id
    @Column(name="transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name="beneficiary_user_id", nullable = false)
    private User beneficiary;

    @ManyToOne()
    @JoinColumn(name="payer_user_id", nullable = false)
    private User payer;

}
