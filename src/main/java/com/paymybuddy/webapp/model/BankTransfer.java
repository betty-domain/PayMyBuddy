package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bank_transfer")
public class BankTransfer {

    @Id
    @Column(name = "bank_transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "transfer_order")
    private BankTransferOrder transferOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_account_id", nullable = false)
    private BankAccount bankAccount;

    public BankTransfer(){}

    public BankTransfer(Integer id, BigDecimal amount, LocalDateTime date, BankTransferOrder bankTransferOrder)
    {
        this.id = id;
        this.amount=amount;
        this.date = date;
        this.transferOrder = bankTransferOrder;
    }

}
