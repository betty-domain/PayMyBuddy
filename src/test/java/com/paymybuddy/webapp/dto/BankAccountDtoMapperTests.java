package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BankAccountDtoMapperTests {

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;


    @Test
    public void mapToBankAcountWithNullObjects()
    {
        assertThat(bankAccountDtoMapper.mapToBankAccount(null,null)).isNull();
    }

    @Test
    public void mapToBankAccount_WithValidData()
    {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("iban");
        bankAccountDto.setId(25);
        bankAccountDto.setUserId(30);

        User user = new User();
        user.setId(bankAccountDto.getUserId());

        BankAccount mappedBankAccount = bankAccountDtoMapper.mapToBankAccount(bankAccountDto,user);

        assertThat(mappedBankAccount.getId()).isEqualTo(bankAccountDto.getId());
        assertThat(mappedBankAccount.getUser()).isEqualTo(user);
        assertThat(mappedBankAccount.getDescription()).isEqualTo(bankAccountDto.getDescription());
        assertThat(mappedBankAccount.getIban()).isEqualTo(bankAccountDto.getIban());
    }
}
