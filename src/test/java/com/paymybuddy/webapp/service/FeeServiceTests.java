package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.FeeDto;
import com.paymybuddy.webapp.model.Fee;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FeeServiceTests {

    @Autowired
    IFeeService feeService;

    @MockBean
    UserRepository userRepositoryMock;

    @Test
    void getAllFeeForUser_NullId()
    {
        Exception exception = assertThrows(FunctionalException.class, () -> {
            feeService.getAllFeeForUser(null);
        });

        assertThat(exception.getMessage()).contains("Données incorrectes");
    }

    @Test
    void getAllFeeForUser_NotExistingUser()
    {
        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FunctionalException.class, () -> {
            feeService.getAllFeeForUser(6);
        });

        assertThat(exception.getMessage()).contains("Utilisateur inexistant");
    }

    @Test
    void getAllFeeForUser_Ok()
    {
        User user = new User();
        user.setId(10);
        user.getFeeList().add(new Fee());
        when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.of(user));

        List<FeeDto> feeDtoList = feeService.getAllFeeForUser(user.getId());

        assertThat(feeDtoList.size()).isEqualTo(1);
    }
}
