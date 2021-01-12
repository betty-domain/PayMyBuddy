package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.model.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer,Integer> {

    List<BankTransfer> findAllByUser_Id(Integer userId);
}
