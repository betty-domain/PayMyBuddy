package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="user", indexes = @Index(name="user_email_idx",columnList = "email"))
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(length = 100,nullable = false)
    private String email;

    @NotNull
    @Size(max=30)
    @Column(length = 30,nullable = false)
    private String firstname;

    @NotNull
    @Size(max=60)
    @Column(length = 60,nullable = false)
    private String lastname;

    @NotNull
    @Size(max=80)
    @Column(length = 80,nullable = false)
    private String password;

    @Column(precision = 12,scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user")
    private List<Friendship> friendshipList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "payer")
    private List<Transaction> transactionOutcomingList= new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "beneficiary")
    private List<Transaction> transactionIncomingList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Fee> feeList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BankAccount> bankAccountList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BankTransfer> bankTransferList= new ArrayList<>();

}
