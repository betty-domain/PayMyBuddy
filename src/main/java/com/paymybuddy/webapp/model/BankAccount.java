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
import java.util.List;

@Data
@Entity
@Table(name="bank_account")
public class BankAccount {

    @Id
    @Column(name = "bank_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String iban;

    @Column(name = "is_actif")
    @NotNull
    private boolean isActif;

    @NotNull
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;


}
