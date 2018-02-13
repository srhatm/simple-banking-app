package com.bankingapp.bankingdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bankingapp.bankingdemo.model.BankClientAction;

public interface BankClientActionRepository extends JpaRepository<BankClientAction, Long> {
	@Query("select b from BankClientAction b where b.bankClient.id = ?1")
    List<BankClientAction> findActionsByClientId(Long clientId);
}
