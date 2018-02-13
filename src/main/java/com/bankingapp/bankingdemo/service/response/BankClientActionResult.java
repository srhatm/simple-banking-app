package com.bankingapp.bankingdemo.service.response;

import java.math.BigDecimal;

public class BankClientActionResult extends Result{
	
	private BigDecimal balance;

	public BankClientActionResult() {
		super();
	}
	
	public BankClientActionResult(BigDecimal balance) {
		super();
		this.balance = balance;
	}



	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
