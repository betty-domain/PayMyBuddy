package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.FeeDto;
import com.paymybuddy.webapp.service.IFeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_TestData.sql")
})
public class FeeIT {

    @Autowired
    IFeeService feeService;

    @Test
    void getAllFeeForUser()
    {
        Integer userId=2;
        List<FeeDto> feeDToList = feeService.getAllFeeForUser(userId);

        assertThat(feeDToList.size()).isGreaterThan(0);
    }

}
