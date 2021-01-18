package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.model.PayMyBuddyConstants;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.IFriendTransactionService;
import com.paymybuddy.webapp.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")
})
public class FriendTransactionIT {

    @Autowired
    IFriendTransactionService transactionService;

    @MockBean
    DateUtils dateUtils;

    @Autowired
    UserRepository userRepository;

    @Test
    void transferToFriend()
    {
        Integer payerId = 3;
        BigDecimal actualPayerBalance = userRepository.findById(payerId).get().getBalance();

        Integer beneficiaryId=2;
        BigDecimal actualBeneficiaryBalance = userRepository.findById(beneficiaryId).get().getBalance();

        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(payerId);
        incomingTransactionDto.setBeneficiaryId(beneficiaryId);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(100));

        LocalDate mockLocalDateNow = LocalDate.of(2012,5,25);
        when(dateUtils.getNowLocalDate()).thenReturn(mockLocalDateNow);

        TransactionDto transactionDto = transactionService.transferToFriend(incomingTransactionDto);

        assertThat(transactionDto).isNotNull();

        //vérification du prélèvement généré sur la transaction effectuée
        assertThat(transactionDto.getFee().getPercentage100()).isEqualTo(PayMyBuddyConstants.FEE_PERCENTAGE100);
        assertThat(transactionDto.getFee().getAmount()).isEqualTo(incomingTransactionDto.getAmount().multiply(PayMyBuddyConstants.FEE_PERCENTAGE100.divide(new BigDecimal(100))));
        assertThat(transactionDto.getFee().getDate()).isEqualTo(mockLocalDateNow);

        //vérification des soldes du payeur et du bénéficiaire
        assertThat(transactionDto.getPayer().getBalance()).isEqualTo(actualPayerBalance.subtract(incomingTransactionDto.getAmount()).subtract(transactionDto.getFee().getAmount()));
        assertThat(transactionDto.getBeneficiary().getBalance()).isEqualTo(actualBeneficiaryBalance.add(incomingTransactionDto.getAmount()));

        //vérification des autres informations sur la transaction
        assertThat(transactionDto.getDate()).isEqualTo(mockLocalDateNow);
        assertThat(transactionDto.getDescription()).isEqualTo(incomingTransactionDto.getDescription());
        assertThat(transactionDto.getAmount()).isEqualTo(incomingTransactionDto.getAmount());
    }
}
