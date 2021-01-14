package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BankTransferListDtoMapperTests {

    @Autowired
    private BankTransferListDtoMapper mapper ;

    @Test
    void mapToBankTransferListDto_WithNullValues()
    {
        assertThat(mapper.mapToBankTransferListDto(null)).isNull();
    }

    @Test
    void mapToBankTransferListDto_ValidMapping()
    {
        //given un utilisateur
        User user = new User();
        user.setBalance(new BigDecimal(250));
        user.setLastname("lastname");
        user.setId(40);
        user.setPassword("myPassword");
        user.setEmail("email@gmail.com");
        user.setFirstname("firstname");

        user.setTransactionList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        //given 1 compte bancaire avec 2 transferts d'argent
        BankAccount bankAccount = new BankAccount(5,"iban",false,"description",user);
        user.getBankAccountList().add(bankAccount);
        List<BankTransfer> bankTransferList = new ArrayList<>();
        BankTransfer bankTransfer = new BankTransfer(452,new BigDecimal(54.25),LocalDate.of(2014,3,12),BankTransferOrder.FROM_BANK);
        bankTransfer.setBankAccount(bankAccount);
        bankTransfer.setUser(user);
        bankTransferList.add(bankTransfer);

        BankTransfer bankTransfer2 = new BankTransfer(220,new BigDecimal(5401),LocalDate.of(2010,1,1),BankTransferOrder.TO_BANK);
        bankTransfer2.setBankAccount(bankAccount);
        bankTransfer2.setUser(user);
        bankTransferList.add(bankTransfer2);

        bankAccount.setBankTransferList(bankTransferList);

        //given 1 compte bancaire avec un transfert d'argent
        BankAccount bankAccount2 = new BankAccount(15,"iban",true,"description",user);
        user.getBankAccountList().add(bankAccount2);
        List<BankTransfer> bankTransferList_2 = new ArrayList<>();
        BankTransfer bankTransfer3 = new BankTransfer(45,new BigDecimal(54.25),LocalDate.of(2014,3,12),BankTransferOrder.FROM_BANK);
        bankTransfer3.setBankAccount(bankAccount2);
        bankTransfer3.setUser(user);
        bankTransferList_2.add(bankTransfer3);

        bankAccount2.setBankTransferList(bankTransferList_2);

        user.getBankTransferList().addAll(bankTransferList);
        user.getBankTransferList().addAll(bankTransferList_2);

        BankTransferListDto bankTransferListDto = mapper.mapToBankTransferListDto(user);

        assertThat(bankTransferListDto.getUserDto().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(bankTransferListDto.getUserDto().getBalance()).isEqualTo(user.getBalance());

        List<BankAccountDto> bankAccountDtoList =bankTransferListDto.getBankAccountDtoList();

        assertThat(bankAccountDtoList.size()).isEqualTo(2);

        assertThat(bankAccountDtoList.get(0).getBankTransferDtoList().size()).isEqualTo(2);
        assertThat(bankAccountDtoList.get(1).getBankTransferDtoList().size()).isEqualTo(1);

    }
}
