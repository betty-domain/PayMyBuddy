package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BankAccountDtoMapperTests {

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;


    @Test
    public void mapToBankAccountWithNullObjects()
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
        bankAccountDto.setBankTransferDtoList(new ArrayList<>());

        User user = new User();
        user.setId(bankAccountDto.getUserId());

        BankAccount mappedBankAccount = bankAccountDtoMapper.mapToBankAccount(bankAccountDto,user);

        assertThat(mappedBankAccount.getId()).isEqualTo(bankAccountDto.getId());
        assertThat(mappedBankAccount.getUser()).isEqualTo(user);
        assertThat(mappedBankAccount.getDescription()).isEqualTo(bankAccountDto.getDescription());
        assertThat(mappedBankAccount.getIban()).isEqualTo(bankAccountDto.getIban());
        assertThat(mappedBankAccount.getBankTransferList()).isNull();
    }

    @Test
    public void mapFromBankAccount_WithNullBankAccount(){
        assertThat(bankAccountDtoMapper.mapFromBankAccount(null)).isNull();
    }

    @Test
    public void mapFromBankAccount_ValidMapping(){
        User user = new User();
        user.setId(40);

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setTransferOrder(BankTransferOrder.FROM_BANK);
        bankTransfer.setUser(user);
        bankTransfer.setDate(LocalDate.of(2014,3,12));
        bankTransfer.setAmount(new BigDecimal(54.25));
        bankTransfer.setId(452);
        List<BankTransfer> bankTransferList = new ArrayList<>();
        bankTransferList.add(bankTransfer);

        BankAccount bankAccount = new BankAccount(5,"iban",false,"description",user);
        bankAccount.setBankTransferList(bankTransferList);

        BankAccountDto bankAccountDto =bankAccountDtoMapper.mapFromBankAccount(bankAccount);

        assertThat(bankAccountDto.getId()).isEqualTo(bankAccount.getId());
        assertThat(bankAccountDto.getDescription()).isEqualTo(bankAccount.getDescription());
        assertThat(bankAccountDto.getIban()).isEqualTo(bankAccount.getIban());
        assertThat(bankAccountDto.getUserId()).isEqualTo(bankAccount.getUser().getId());
        assertThat(bankAccountDto.getBankTransferDtoList().size()).isEqualTo(bankAccount.getBankTransferList().size());
    }
}
