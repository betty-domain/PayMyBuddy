package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @NotEmpty
    @Size(max = 34)
    private String iban;

    @Column(name = "is_actif")
    @NotNull
    @JsonIgnore
    private boolean isActif = true;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "bankAccount")
    List<BankTransfer> bankTransferList;

    public BankAccount()
    {

    }

    public BankAccount(Integer id, String iban, boolean isActif, String description, User user )
    {
        this.id = id;
        this.iban= iban;
        this.isActif = isActif;
        this.description  = description;
        this.user=user;
    }
}
