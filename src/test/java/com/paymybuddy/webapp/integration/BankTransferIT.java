package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.BankTransferRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.IBankTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")
})
class BankTransferIT {

    @Autowired
    UserRepository userRepositorySpy;

    @Autowired
    IBankTransferService bankTransferService;

    @Autowired
    BankTransferRepository bankTransferRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Test
    void transferToBank() {

        Integer userId = 3;
        List<BankTransfer> existingBankTransferList = bankTransferRepository.findAllByUser_IdOrderByDateDesc(userId);

        User existingUser = userRepositorySpy.findById(userId).get();
        BigDecimal actualAmount = existingUser.getBalance();

        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(existingUser.getBankAccountList().get(0).getId());
        bankTransferDto.setAmount(new BigDecimal(20));

        BankTransfer bankTransfer = bankTransferService.transferToBank(bankTransferDto);
        List<BankTransfer> updatingBankTransferList = bankTransferRepository.findAllByUser_IdOrderByDateDesc(userId);
        User updatingUser = userRepositorySpy.findById(userId).get();

        assertThat(bankTransfer.getAmount()).isEqualTo(bankTransferDto.getAmount());
        assertThat(bankTransfer.getUser()).isEqualTo(existingUser);
        assertThat(bankTransfer.getTransferOrder()).isEqualTo(BankTransferOrder.TO_BANK);
        assertThat(updatingBankTransferList.size()).isEqualTo(existingBankTransferList.size() + 1);
        assertThat(updatingUser.getBalance()).isEqualTo(actualAmount.subtract(bankTransferDto.getAmount()));

    }

    @Test
    void transferFromBank() {


        Integer userId = 3;
        List<BankTransfer> existingBankTransferList = bankTransferRepository.findAllByUser_IdOrderByDateDesc(userId);
        User existingUser = userRepositorySpy.findById(userId).get();

        BigDecimal actualAmount = existingUser.getBalance();

        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(existingUser.getBankAccountList().get(0).getId());
        bankTransferDto.setAmount(new BigDecimal(250));

        BankTransfer bankTransfer = bankTransferService.transferFromBank(bankTransferDto);

        List<BankTransfer> updatingBankTransferList = bankTransferRepository.findAllByUser_IdOrderByDateDesc(userId);
        User updatingUser = userRepositorySpy.findById(userId).get();

        assertThat(bankTransfer.getAmount()).isEqualTo(bankTransferDto.getAmount());
        assertThat(bankTransfer.getUser()).isEqualTo(existingUser);
        assertThat(bankTransfer.getTransferOrder()).isEqualTo(BankTransferOrder.FROM_BANK);
        assertThat(updatingBankTransferList.size()).isEqualTo(existingBankTransferList.size() + 1);
        assertThat(updatingUser.getBalance()).isEqualTo(actualAmount.add(bankTransferDto.getAmount()));

    }
}
