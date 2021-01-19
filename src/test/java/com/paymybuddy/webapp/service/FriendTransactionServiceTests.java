package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.model.Fee;
import com.paymybuddy.webapp.model.Friendship;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.FeeRepository;
import com.paymybuddy.webapp.repository.FriendTransactionRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FriendTransactionServiceTests {

    @Autowired
    IFriendTransactionService transactionService;

    @MockBean
    UserRepository userRepositoryMock;

    @MockBean
    FeeRepository feeRepositoryMock;

    @MockBean
    DateUtils dateUtils;
    
    @MockBean
    FriendTransactionRepository friendTransactionRepositoryMock;

    @Test
    void transferToFriend_NullDto()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void transferToFriend_InvalidDto()
    {
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(incomingTransactionDto);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void transferToFriend_PayerNotExist()
    {
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(5);
        incomingTransactionDto.setBeneficiaryId(10);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(15.50));

        when(userRepositoryMock.findById(incomingTransactionDto.getBeneficiaryId())).thenReturn(Optional.of(new User()));
        when(userRepositoryMock.findById(incomingTransactionDto.getPayerId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(incomingTransactionDto);
        });

        assertThat(exception.getMessage()).contains("Payeur inexistant");
    }

    @Test
    void transferToFriend_BeneficiaryNotExist()
    {
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(5);
        incomingTransactionDto.setBeneficiaryId(10);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(15.50));

        when(userRepositoryMock.findById(incomingTransactionDto.getPayerId())).thenReturn(Optional.of(new User()));
        when(userRepositoryMock.findById(incomingTransactionDto.getBeneficiaryId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(incomingTransactionDto);
        });

        assertThat(exception.getMessage()).contains("Bénéficiaire inexistant");
    }

    @Test
    void transferToFriend_BeneficiaryIsNotAFriend()
    {
        Integer payerId = 5;
        Integer beneficiaryId=10;
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(payerId);
        incomingTransactionDto.setBeneficiaryId(beneficiaryId);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(15.50));

        User payer = new User();
        payer.setId(payerId);
        payer.setBalance(new BigDecimal(500));

        User beneficiary = new User();
        beneficiary.setId(beneficiaryId);
        beneficiary.setBalance(new BigDecimal(500));

        when(userRepositoryMock.findById(incomingTransactionDto.getPayerId())).thenReturn(Optional.of(payer));
        when(userRepositoryMock.findById(incomingTransactionDto.getBeneficiaryId())).thenReturn(Optional.of(beneficiary));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(incomingTransactionDto);
        });

        assertThat(exception.getMessage()).contains("Cette personne n'est pas un ami du payeur");
    }

    @Test
    void transferToFriend_NotSufficientBalance()
    {
        Integer payerId = 5;
        Integer beneficiaryId=10;
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(payerId);
        incomingTransactionDto.setBeneficiaryId(beneficiaryId);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(15.50));

        User payer = new User();
        payer.setId(payerId);
        payer.setBalance(new BigDecimal(5));

        User beneficiary = new User();
        beneficiary.setId(beneficiaryId);
        beneficiary.setBalance(new BigDecimal(500));

        Friendship friendship = new Friendship();
        friendship.setUser(payer);
        friendship.setAmi(beneficiary);
        friendship.setActif(true);

        payer.getFriendshipList().add(friendship);

        when(userRepositoryMock.findById(incomingTransactionDto.getPayerId())).thenReturn(Optional.of(payer));
        when(userRepositoryMock.findById(incomingTransactionDto.getBeneficiaryId())).thenReturn(Optional.of(beneficiary));

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.transferToFriend(incomingTransactionDto);
        });

        assertThat(exception.getMessage()).contains("Solde insuffisant pour réaliser la transaction avec les frais");
    }

    @Test
    void transferToFriend_isOk()
    {
        Integer payerId = 5;
        Integer beneficiaryId=10;
        IncomingTransactionDto incomingTransactionDto = new IncomingTransactionDto();
        incomingTransactionDto.setPayerId(payerId);
        incomingTransactionDto.setBeneficiaryId(beneficiaryId);
        incomingTransactionDto.setDescription("description");
        incomingTransactionDto.setAmount(new BigDecimal(15.50));

        User payer = new User();
        payer.setId(payerId);
        payer.setBalance(new BigDecimal(500));

        User beneficiary = new User();
        beneficiary.setId(beneficiaryId);
        beneficiary.setBalance(new BigDecimal(500));

        Friendship friendship = new Friendship();
        friendship.setUser(payer);
        friendship.setAmi(beneficiary);
        friendship.setActif(true);

        payer.getFriendshipList().add(friendship);

        when(userRepositoryMock.findById(incomingTransactionDto.getPayerId())).thenReturn(Optional.of(payer));
        when(userRepositoryMock.findById(incomingTransactionDto.getBeneficiaryId())).thenReturn(Optional.of(beneficiary));
        LocalDateTime mockLocalDateNow = LocalDateTime.of(2012,5,25,15,10,0);
        when(dateUtils.getNowLocalDateTime()).thenReturn(mockLocalDateNow);

        when(feeRepositoryMock.save(any())).thenReturn(new Fee());

        TransactionDto transactionDto = transactionService.transferToFriend(incomingTransactionDto);

        assertThat(transactionDto).isNotNull();

        /*
        //vérification du prélèvement généré sur la transaction effectuée
        assertThat(transactionDto.getFee().getPercentage100()).isEqualTo(PayMyBuddyConstants.FEE_PERCENTAGE100);
        assertThat(transactionDto.getFee().getAmount()).isEqualTo(PayMyBuddyConstants.FEE_PERCENTAGE100.multiply(incomingTransactionDto.getAmount()));
        assertThat(transactionDto.getFee().getDate()).isEqualTo(mockLocalDateNow);

        //vérification des soldes du payeur et du bénéficiaire
        assertThat(transactionDto.getPayer().getBalance()).isEqualTo(payer.getBalance().subtract(incomingTransactionDto.getAmount()).subtract(transactionDto.getFee().getAmount()));
        assertThat(transactionDto.getPayer().getBalance()).isEqualTo(beneficiary.getBalance().add(incomingTransactionDto.getAmount()));

        //vérification des autres informations sur la transaction
        assertThat(transactionDto.getDate()).isEqualTo(mockLocalDateNow);
        assertThat(transactionDto.getDescription()).isEqualTo(incomingTransactionDto.getDescription());
        assertThat(transactionDto.getAmount()).isEqualTo(incomingTransactionDto.getAmount());
    */
    }

    @Test
    void getAllTransactionForUser_NullId()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.getAllTransactionForUser(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void getAllTransactionForUser_NotExistingUser()
    {
        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            transactionService.getAllTransactionForUser(6);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void getAllTransactionForUser_Ok()
    {
        User user = new User();
        user.setId(10);
        user.getFeeList().add(new Fee());
        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.of(user));

        List<TransactionDto> transactionDto = transactionService.getAllTransactionForUser(user.getId());

        assertThat(transactionDto.size()).isEqualTo(1);
    }

}
