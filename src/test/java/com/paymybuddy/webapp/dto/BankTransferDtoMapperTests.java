package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankTransferDtoMapperTests {

    @Autowired
    private BankTransferDtoMapper bankTransferDtoMapper;

    @MockBean
    private DateUtils  dateUtils;

    @Test
    public void mapToBankTransferWithNullObjects()
    {
        assertThat(bankTransferDtoMapper.mapToBankTransfer(null,null)).isNull();
    }

    @Test
    public void mapToBankTransferWithValidObjects()
    {
        int userId= 52;
        int bankAccountId=45;

        LocalDate localDateNowMock = LocalDate.of(2015,10,25);
        when(dateUtils.getNowLocalDate()).thenReturn(localDateNowMock);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(new User());
        bankAccount.getUser().setId(userId);
        bankAccount.setActif(true);
        bankAccount.setDescription("description");
        bankAccount.setId(bankAccountId);
        bankAccount.setIban("myIban");

        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setAmount(new BigDecimal(250.58));
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(bankAccountId);

        BankTransfer bankTransfer = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto,bankAccount);

        assertThat(bankTransfer.getBankAccount()).isEqualTo(bankAccount);
        assertThat(bankTransfer.getAmount()).isEqualTo(bankTransferDto.getAmount());
        assertThat(bankTransfer.getDate()).isEqualTo(localDateNowMock);
        assertThat(bankTransfer.getUser()).isEqualTo(bankAccount.getUser());
        assertThat(bankTransfer.getId()).isNull();
        assertThat(bankTransfer.getTransferOrder()).isNull();

    }
}
