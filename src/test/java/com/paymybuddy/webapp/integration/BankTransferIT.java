package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankTransferRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.IBankTransferService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:removeData.sql")
})
public class BankTransferIT {

    @Autowired
    UserRepository userRepositorySpy;

    @Autowired
    IBankTransferService bankTransferService;

    @Autowired
    BankTransferRepository bankTransferRepository;

    @Test
    @Disabled
    public void transferFromBank_ExceptionWhenSavingUser() {
//TODO : voir commetn tester qu'il y a bien un rollback si une partie du traitemen génère une erreur : ici, voir comment simuler une erreur de l'une des requêtes d'insert ou d'update
        Integer userId = 3;
        List<BankTransfer> existingBankTransferList = bankTransferRepository.findAllByUser_Id(userId);

        given(userRepositorySpy.save(any())).willAnswer(invocation -> {
            throw new Exception();
        });

        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(3);
        bankTransferDto.setAmount(new BigDecimal(250));

        Exception exception = assertThrows(Exception.class, () -> {
            bankTransferService.transferFromBank(bankTransferDto);
        });

        List<BankTransfer> updatingBankTransferList = bankTransferRepository.findAllByUser_Id(userId);

        assertThat(existingBankTransferList).isEqualTo(updatingBankTransferList);

    }

    @Test
    public void transferFromBank_ValidTransaction() {

        Integer userId = 3;
        List<BankTransfer> existingBankTransferList = bankTransferRepository.findAllByUser_Id(userId);
        User existingUser = userRepositorySpy.findById(userId).get();

        BigDecimal actualAmount = existingUser.getBalance();

        BankTransferDto bankTransferDto = new BankTransferDto();
        bankTransferDto.setUserId(userId);
        bankTransferDto.setBankAccountId(3);
        bankTransferDto.setAmount(new BigDecimal(250));

        BankTransfer bankTransfer = bankTransferService.transferFromBank(bankTransferDto);

        List<BankTransfer> updatingBankTransferList = bankTransferRepository.findAllByUser_Id(userId);
        User updatingUser = userRepositorySpy.findById(userId).get();

        assertThat(bankTransfer.getAmount()).isEqualTo(bankTransferDto.getAmount());
        assertThat(bankTransfer.getUser()).isEqualTo(existingUser);
        assertThat(bankTransfer.getTransferOrder()).isEqualTo(BankTransferOrder.FROM_BANK);
        assertThat(updatingBankTransferList.size()).isEqualTo(existingBankTransferList.size() + 1);
        assertThat(updatingUser.getBalance()).isEqualTo(actualAmount.add(bankTransferDto.getAmount()));

    }
}
