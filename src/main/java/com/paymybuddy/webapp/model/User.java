package com.paymybuddy.webapp.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.jws.soap.SOAPBinding;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name="user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String email;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String password;

    @NotNull
    private BigDecimal balance;

    @OneToMany(mappedBy = "user")
    List<Friendship> friendshipList;

    @OneToMany(mappedBy = "payer")
    List<Transaction> transactionList;

    //TODO : voir si cette propriété sera utilisée ou non à partir de l'objet utilisateur
    @OneToMany(mappedBy = "user")
    List<BankAccount> bankAccountList;

    //TODO : voir si cette propriété sera utilisée ou non à partir de l'objet utilisateur
    @OneToMany(mappedBy = "user")
    List<BankTransfer> bankTransferList;
}
