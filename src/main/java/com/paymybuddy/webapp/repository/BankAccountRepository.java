package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {

    Optional<BankAccount> findByIdAndUser_id(Integer bankAccountId, Integer userId);

    Optional<BankAccount> findByIbanAndUser_Id(String iban, Integer userId);
}
