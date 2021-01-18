package com.paymybuddy.webapp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name= "transaction")
public class Transaction {

    @Id
    @Column(name="transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = 200)
    @Column(length = 200,nullable = false)
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="beneficiary_user_id", nullable = false)
    private User beneficiary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payer_user_id", nullable = false)
    private User payer;

    @OneToMany(mappedBy = "transaction")
    List<Fee> feeList;

}
