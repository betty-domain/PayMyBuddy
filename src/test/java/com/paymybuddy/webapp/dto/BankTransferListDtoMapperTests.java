package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BankTransferListDtoMapperTests {

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

        user.setTransactionIncomingList(new ArrayList<>());
        user.setTransactionOutcomingList(new ArrayList<>());
        user.setFriendshipList(new ArrayList<>());
        user.setBankAccountList(new ArrayList<>());
        user.setBankTransferList(new ArrayList<>());

        BankTransferListDto bankTransferListDto = mapper.mapToBankTransferListDto(user);

        assertThat(bankTransferListDto.getUserDto().getFirstname()).isEqualTo(user.getFirstname());
        assertThat(bankTransferListDto.getUserDto().getBalance()).isEqualTo(user.getBalance());
    }
}
