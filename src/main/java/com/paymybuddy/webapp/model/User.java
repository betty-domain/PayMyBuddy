package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

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
    private BigDecimal balance = new BigDecimal(0);

    @OneToMany(mappedBy = "user")
    List<Friendship> friendshipList;

    @JsonIgnore
    @OneToMany(mappedBy = "payer")
    List<Transaction> transactionList;

    //TODO : voir si cette propriété sera utilisée ou non à partir de l'objet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<BankAccount> bankAccountList;

    //TODO : voir si cette propriété sera utilisée ou non à partir de l'objet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<BankTransfer> bankTransferList;
}
