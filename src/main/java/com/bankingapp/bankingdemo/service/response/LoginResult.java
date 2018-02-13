package com.bankingapp.bankingdemo.service.response;

public class LoginResult extends Result{
	private Long id;
	
	public LoginResult() {
		super();
	}
	
	public LoginResult(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
