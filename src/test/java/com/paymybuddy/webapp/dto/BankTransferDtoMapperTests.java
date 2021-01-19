package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        LocalDateTime localDateNowMock = LocalDateTime.of(2015,10,25,15,10,0);
        when(dateUtils.getNowLocalDateTime()).thenReturn(localDateNowMock);

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
        bankTransferDto.setDate(LocalDateTime.of(2000,1,1,15,10,0));
        bankTransferDto.setTransferOrder(BankTransferOrder.TO_BANK);

        BankTransfer bankTransfer = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto,bankAccount);

        assertThat(bankTransfer.getBankAccount()).isEqualTo(bankAccount);
        assertThat(bankTransfer.getAmount()).isEqualTo(bankTransferDto.getAmount());
        assertThat(bankTransfer.getDate()).isEqualTo(localDateNowMock);
        assertThat(bankTransfer.getUser()).isEqualTo(bankAccount.getUser());
        assertThat(bankTransfer.getId()).isNull();
        assertThat(bankTransfer.getTransferOrder()).isEqualTo(bankTransferDto.getTransferOrder());

    }

    @Test
    public void mapFromBankTransfer_WithNullObject()
    {
        assertThat(bankTransferDtoMapper.mapFromBankTransfer(null)).isNull();
    }

    @Test
    public void mapFromBankTransfer_ValidMapping()
    {
        User user = new User();
        user.setId(47);

        BankAccount bankAccount = new BankAccount(15,"iban",false,"description", null);

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setTransferOrder(BankTransferOrder.TO_BANK);
        bankTransfer.setAmount(new BigDecimal(54.25));
        bankTransfer.setDate(LocalDateTime.of(2020,10,15,15,10,0));
        bankTransfer.setId(42);
        bankTransfer.setBankAccount(bankAccount);
        bankTransfer.setUser(user);

        BankTransferDto bankTransferDto = bankTransferDtoMapper.mapFromBankTransfer(bankTransfer);

        assertThat(bankTransferDto.getTransferOrder()).isEqualTo(bankTransfer.getTransferOrder());
        assertThat(bankTransferDto.getAmount()).isEqualTo(bankTransfer.getAmount());
        assertThat(bankTransferDto.getDate()).isEqualTo(bankTransfer.getDate());
        assertThat(bankTransferDto.getBankAccountId()).isEqualTo(bankAccount.getId());
        assertThat(bankTransferDto.getUserId()).isEqualTo(user.getId());
    }

    @Test
    public void mapFromBankTransferList_WithNullObject()
    {
        assertThat(bankTransferDtoMapper.mapFromBankTransferList(null)).isNull();
    }

    @Test
    public void mapFromBankTransferList_ValidMapping()
    {
        User user = new User();
        user.setId(47);

        BankAccount bankAccount = new BankAccount(15,"iban",false,"description", null);

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setTransferOrder(BankTransferOrder.TO_BANK);
        bankTransfer.setAmount(new BigDecimal(54.25));
        bankTransfer.setDate(LocalDateTime.of(2020,10,15,15,10,0));
        bankTransfer.setId(42);
        bankTransfer.setBankAccount(bankAccount);
        bankTransfer.setUser(user);

        List<BankTransfer> bankTransferList = new ArrayList<>();
        bankTransferList.add(bankTransfer);
        bankTransferList.add(bankTransfer);

        BankTransferDto bankTransferDto = bankTransferDtoMapper.mapFromBankTransfer(bankTransfer);

        assertThat(bankTransferDto.getTransferOrder()).isEqualTo(bankTransfer.getTransferOrder());
        assertThat(bankTransferDto.getAmount()).isEqualTo(bankTransfer.getAmount());
        assertThat(bankTransferDto.getDate()).isEqualTo(bankTransfer.getDate());
        assertThat(bankTransferDto.getBankAccountId()).isEqualTo(bankAccount.getId());
        assertThat(bankTransferDto.getUserId()).isEqualTo(user.getId());
    }

}
