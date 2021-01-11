package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class BankAccountDto {

    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = 34)
    private String iban;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String description;

    @NotNull
    private Integer userId;

    /**
     * Vérifie si l'objet est valide
     * @return true si valide, false sinon
     */
    public boolean isValid()
    {
        if (iban ==null || iban.isEmpty())
        {
            return false;
        }
        if (description==null || description.isEmpty())
        {
            return false;
        }
        if (userId==null)
        {
            return false;
        }
        return true;
    }
}
