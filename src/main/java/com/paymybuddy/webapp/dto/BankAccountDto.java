package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BankAccountDto {

    @JsonView(DtoJsonView.Private.class)
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(max = 34)
    @JsonView(DtoJsonView.Public.class)
    private String iban;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    @JsonView(DtoJsonView.Public.class)
    private String description;

    @NotNull
    @JsonView(DtoJsonView.Private.class)
    private Integer userId;

    @JsonProperty("bankTransferList")
    @JsonView(DtoJsonView.Public.class)
    private List<BankTransferDto> bankTransferDtoList;


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
