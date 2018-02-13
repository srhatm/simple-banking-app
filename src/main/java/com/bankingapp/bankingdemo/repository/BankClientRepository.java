package com.bankingapp.bankingdemo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bankingapp.bankingdemo.model.BankClient;

@Repository
public interface BankClientRepository extends JpaRepository<BankClient, Long> {
	
	@Query("select b from BankClient b where b.email = ?1 and b.password = ?2")
    List<BankClient> findByEmailAndPassword(String email, String password);
	
	@Query("select b.balance from BankClient b where b.id = ?1")
    BigDecimal findBalanceByClientId(Long id);
}
