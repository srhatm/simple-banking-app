package com.bankingapp.bankingdemo.service.response;

import java.math.BigDecimal;
import java.util.List;

import com.bankingapp.bankingdemo.model.BankClientAction;

public class BalanceStatementResult extends Result{

	private BigDecimal balance;
	private List<BankClientAction> actions;
	
	public BalanceStatementResult() {
		super();
	}
	
	public BalanceStatementResult(BigDecimal balance, List<BankClientAction> actions) {
		super();
		this.balance = balance;
		this.actions = actions;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<BankClientAction> getActions() {
		return actions;
	}
	public void setActions(List<BankClientAction> actions) {
		this.actions = actions;
	}
	
	
}
