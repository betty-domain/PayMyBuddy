package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {
}
