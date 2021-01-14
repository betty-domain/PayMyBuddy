package com.paymybuddy.webapp.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class UserDto {

    @NotNull
    @NotEmpty
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String firstname;

    @NotNull
    @NotEmpty
    @Size(max = 60)
    private String lastname;

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String password;

    private BigDecimal balance;

    /**
     * Vérifie que les propriétés de l'entité UserDto sont valides fonctionnellement
     * @return true si valide, false sinon
     */
    public boolean isValid() {

        if (this.getEmail() == null || this.getEmail().isEmpty()) {
            return false;
        }
        if (this.getFirstname() == null || this.getFirstname().isEmpty()) {
            return false;
        }
        if (this.getLastname() == null || this.getLastname().isEmpty()) {
            return false;
        }
        if (this.getPassword() == null || this.getPassword().isEmpty()) {
            return false;
        }
        return true;

    }
}
