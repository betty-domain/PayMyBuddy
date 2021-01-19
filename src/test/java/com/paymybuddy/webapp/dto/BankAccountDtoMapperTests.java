package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BankAccountDtoMapperTests {

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;

    @Test
    void mapToBankAccountWithNullObjects() {
        assertThat(bankAccountDtoMapper.mapToBankAccount(null, null)).isNull();
    }

    @Test
    void mapToBankAccount_WithValidData() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setDescription("description");
        bankAccountDto.setIban("iban");
        bankAccountDto.setId(25);
        bankAccountDto.setUserId(30);
        bankAccountDto.setBankTransferDtoList(new ArrayList<>());

        User user = new User();
        user.setId(bankAccountDto.getUserId());

        BankAccount mappedBankAccount = bankAccountDtoMapper.mapToBankAccount(bankAccountDto, user);

        assertThat(mappedBankAccount.getId()).isEqualTo(bankAccountDto.getId());
        assertThat(mappedBankAccount.getUser()).isEqualTo(user);
        assertThat(mappedBankAccount.getDescription()).isEqualTo(bankAccountDto.getDescription());
        assertThat(mappedBankAccount.getIban()).isEqualTo(bankAccountDto.getIban());
        assertThat(mappedBankAccount.getBankTransferList()).isNull();
    }

    @Test
    void mapFromBankAccount_WithNullBankAccount() {
        assertThat(bankAccountDtoMapper.mapFromBankAccount(null)).isNull();
    }

    @Test
    void mapFromBankAccount_ValidMapping() {
        User user = new User();
        user.setId(40);

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setTransferOrder(BankTransferOrder.FROM_BANK);
        bankTransfer.setUser(user);
        bankTransfer.setDate(LocalDateTime.of(2014, 3, 12,15,10,0));
        bankTransfer.setAmount(new BigDecimal(54.25));
        bankTransfer.setId(452);
        List<BankTransfer> bankTransferList = new ArrayList<>();
        bankTransferList.add(bankTransfer);

        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", user);
        bankAccount.setBankTransferList(bankTransferList);

        BankAccountDto bankAccountDto = bankAccountDtoMapper.mapFromBankAccount(bankAccount);

        assertThat(bankAccountDto.getId()).isEqualTo(bankAccount.getId());
        assertThat(bankAccountDto.getDescription()).isEqualTo(bankAccount.getDescription());
        assertThat(bankAccountDto.getIban()).isEqualTo(bankAccount.getIban());
        assertThat(bankAccountDto.getUserId()).isEqualTo(bankAccount.getUser().getId());
        assertThat(bankAccountDto.getBankTransferDtoList().size()).isEqualTo(bankAccount.getBankTransferList().size());
    }

    @Test
    void mapListFromBankAccountList_NullObject() {
        assertThat(bankAccountDtoMapper.mapListFromBankAccountList(null)).isNull();
    }

    @Test
    void mapListFromBankAccountList_ValidMapping() {
        List<BankAccount> bankAccountList = new ArrayList<>();

        //given 1 compte bancaire avec 2 transferts d'argent
        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", new User());

        List<BankTransfer> bankTransferList = new ArrayList<>();
        BankTransfer bankTransfer = new BankTransfer(452, new BigDecimal(54.25), LocalDateTime.of(2014, 3, 12,15,10,0), BankTransferOrder.FROM_BANK);
        bankTransfer.setBankAccount(bankAccount);
        bankTransferList.add(bankTransfer);

        BankTransfer bankTransfer2 = new BankTransfer(220, new BigDecimal(5401), LocalDateTime.of(2010, 1, 1,15,10,0), BankTransferOrder.TO_BANK);
        bankTransfer2.setBankAccount(bankAccount);
        bankTransferList.add(bankTransfer2);

        bankAccount.setBankTransferList(bankTransferList);
        bankAccountList.add(bankAccount);

        //given 1 compte bancaire avec un transfert d'argent
        BankAccount bankAccount2 = new BankAccount(15, "iban", true, "description", new User());
        List<BankTransfer> bankTransferList_2 = new ArrayList<>();
        BankTransfer bankTransfer3 = new BankTransfer(45, new BigDecimal(54.25), LocalDateTime.of(2014, 3, 12,15,10,0), BankTransferOrder.FROM_BANK);
        bankTransfer3.setBankAccount(bankAccount2);
        bankTransferList_2.add(bankTransfer3);

        bankAccount2.setBankTransferList(bankTransferList_2);
        bankAccountList.add(bankAccount2);

        List<BankAccountDto> bankAccountDtoList = bankAccountDtoMapper.mapListFromBankAccountList(bankAccountList);

        assertThat(bankAccountDtoList.size()).isEqualTo(2);

        assertThat(bankAccountDtoList.get(0).getBankTransferDtoList().size()).isEqualTo(2);
        assertThat(bankAccountDtoList.get(1).getBankTransferDtoList().size()).isEqualTo(1);

    }

    @Test
    void updateBankAccountWithDto_NullValues()
    {
        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", new User());
        bankAccount.setBankTransferList(new ArrayList<>());

        bankAccountDtoMapper.updateBankAccountFromBankAccountDto(null,bankAccount);
        assertThat(bankAccount.isActif()).isFalse();
        assertThat(bankAccount.getId()).isEqualTo(5);
        assertThat(bankAccount.getIban()).isEqualTo("iban");
        assertThat(bankAccount.getDescription()).isEqualTo("description");
        assertThat(bankAccount.getBankTransferList()).isNotNull();
        assertThat(bankAccount.getUser()).isNotNull();
    }

    @Test
    void updateBankAccountWithDto_NullProperties()
    {
        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", new User());
        bankAccount.setBankTransferList(new ArrayList<>());

        BankAccountDto  bankAccountDto = new BankAccountDto();

        bankAccountDtoMapper.updateBankAccountFromBankAccountDto(bankAccountDto,bankAccount);
        assertThat(bankAccount.isActif()).isFalse();
        assertThat(bankAccount.getId()).isEqualTo(5);
        assertThat(bankAccount.getIban()).isEqualTo("iban");
        assertThat(bankAccount.getDescription()).isEqualTo("description");
        assertThat(bankAccount.getBankTransferList()).isNotNull();
        assertThat(bankAccount.getUser()).isNotNull();
    }

    @Test
    void updateBankAccountWithDto_ValidMapping()
    {
        User user = new User();
        user.setId(1);
        BankAccount bankAccount = new BankAccount(5, "iban", false, "description", user);
        bankAccount.setBankTransferList(new ArrayList<>());

        BankAccountDto  bankAccountDto = new BankAccountDto();
        List<BankTransferDto> bankTransferDtoList = new ArrayList<>();
        bankTransferDtoList.add(new BankTransferDto());
        bankAccountDto.setBankTransferDtoList(bankTransferDtoList);
        bankAccountDto.setIban("iban modified");
        bankAccountDto.setDescription("description modified");
        bankAccountDto.setId(25);
        bankAccountDto.setUserId(43);

        bankAccountDtoMapper.updateBankAccountFromBankAccountDto(bankAccountDto,bankAccount);
        assertThat(bankAccount.isActif()).isEqualTo(false);
        assertThat(bankAccount.getId()).isEqualTo(bankAccountDto.getId());
        assertThat(bankAccount.getIban()).isEqualTo(bankAccountDto.getIban());
        assertThat(bankAccount.getDescription()).isEqualTo(bankAccountDto.getDescription());
        assertThat(bankAccount.getBankTransferList().size()).isEqualTo(0);
        assertThat(bankAccount.getUser().getId()).isEqualTo(1);
    }
}
