package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendTransactionRepository extends JpaRepository<Transaction, Integer> {
}
