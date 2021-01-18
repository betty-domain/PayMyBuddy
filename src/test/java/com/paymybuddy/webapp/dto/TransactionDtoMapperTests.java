package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.Fee;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionDtoMapperTests {

    @Autowired
    TransactionDtoMapper transactionDtoMapper;

    @Test
    void mapFromTransaction_WithNullObject()
    {
        assertThat(transactionDtoMapper.mapFromFee(null)).isNull();
    }

    /*
    @Test
    void mapFromTransaction_WithNullTransaction()
    {
        Fee fee = new Fee();
        fee.setAmount(new BigDecimal(25.52));
        fee.setDate(LocalDate.of(2020,5,10));
        fee.setId(5);
        fee.setPercentage100(new BigDecimal(25));

        TransactionDto transactionDto = transactionDtoMapper.mapFromTransaction(null,fee);

        assertThat(transactionDto.getBeneficiary()).isNull();
        assertThat(transactionDto.getPayer()).isNull();
        assertThat(transactionDto.getAmount()).isNull();
        assertThat(transactionDto.getDate()).isNull();
        assertThat(transactionDto.getDescription()).isNull();
        assertThat(transactionDto.getFee().getPercentage100()).isEqualTo(fee.getPercentage100());
        assertThat(transactionDto.getFee().getAmount()).isEqualTo(fee.getAmount());
        assertThat(transactionDto.getFee().getDate()).isEqualTo(fee.getDate());
    }

    @Test
    void mapFromTransaction_WithNullFee()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(15.5));
        transaction.setDate(LocalDate.of(2020,4,12));
        transaction.setDescription("description");
        User user = new User();
        user.setFirstname("firstname");

        User beneficiary = new User();
        beneficiary.setFirstname("beneficiary");

        transaction.setPayer(user);
        transaction.setBeneficiary(beneficiary);

        TransactionDto transactionDto= transactionDtoMapper.mapFromTransaction(transaction,null);
        assertThat(transactionDto.getFee()).isNull();
        assertThat(transactionDto.getPayer().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(transactionDto.getBeneficiary().getFirstname()).isEqualTo(beneficiary.getFirstname());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getDate()).isEqualTo(transaction.getDate());
        assertThat(transactionDto.getDescription()).isEqualTo(transaction.getDescription());
    }*/

    @Test
    void mapFromTransaction_CheckAllFields()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(15.5));
        transaction.setDate(LocalDate.of(2020,4,12));
        transaction.setDescription("description");
        User user = new User();
        user.setFirstname("firstname");

        User beneficiary = new User();
        beneficiary.setFirstname("beneficiary");

        transaction.setPayer(user);
        transaction.setBeneficiary(beneficiary);


        Fee fee = new Fee();
        fee.setAmount(new BigDecimal(25.52));
        fee.setDate(LocalDate.of(2020,5,10));
        fee.setId(5);
        fee.setPercentage100(new BigDecimal(25));
        fee.setTransaction(transaction);

        TransactionDto transactionDto = transactionDtoMapper.mapFromFee(fee);

        assertThat(transactionDto.getPayer().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(transactionDto.getBeneficiary().getFirstname()).isEqualTo(beneficiary.getFirstname());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getDate()).isEqualTo(transaction.getDate());
        assertThat(transactionDto.getDescription()).isEqualTo(transaction.getDescription());

        assertThat(transactionDto.getFee().getPercentage100()).isEqualTo(fee.getPercentage100());
        assertThat(transactionDto.getFee().getAmount()).isEqualTo(fee.getAmount());
        assertThat(transactionDto.getFee().getDate()).isEqualTo(fee.getDate());

    }


}

