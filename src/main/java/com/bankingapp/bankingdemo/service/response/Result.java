package com.bankingapp.bankingdemo.service.response;

public class Result{

	private int success = 1;
	private String message = "";
	
	public Result() {
	}
	
	public Result(int success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
