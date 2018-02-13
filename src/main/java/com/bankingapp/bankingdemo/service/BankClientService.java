package com.bankingapp.bankingdemo.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapp.bankingdemo.model.ActionType;
import com.bankingapp.bankingdemo.model.BankClient;
import com.bankingapp.bankingdemo.model.BankClientAction;
import com.bankingapp.bankingdemo.repository.BankClientActionRepository;
import com.bankingapp.bankingdemo.repository.BankClientRepository;
import com.bankingapp.bankingdemo.service.response.BalanceStatementResult;
import com.bankingapp.bankingdemo.service.response.BankClientActionResult;
import com.bankingapp.bankingdemo.service.response.LoginResult;
import com.bankingapp.bankingdemo.service.response.Result;
import com.bankingapp.bankingdemo.util.Messages;

@RestController
@RequestMapping(value="/rest/banking")
public class BankClientService {
	
	@Autowired
	BankClientRepository bankClientRepository;
	
	@Autowired
	BankClientActionRepository bankClientActionsRepository;
	
	@GetMapping("/clients")
	public List<BankClient> getAllBankClients() {
	    return bankClientRepository.findAll();
	}
	
	@PostMapping("/add")
	public BankClient createClient(@Valid @RequestBody BankClient bankClient) {
	    return bankClientRepository.save(bankClient);
	}
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public Result login(@RequestParam(value="email") String email, @RequestParam(value="password") String password
	      ){
		List<BankClient> clients = bankClientRepository.findByEmailAndPassword(email, password);
		if(clients != null && clients.size() == 1){
			return new LoginResult(clients.get(0).getId());
		}
		return new Result(0, Messages.ERROR_INVALID_LOGIN);
	}
	
	@PostMapping("/deposit")
	@Transactional
	public Result deposit(@RequestParam(value="id") Long id, @RequestParam(value="amount") BigDecimal amount) {
		BankClient bankClient = bankClientRepository.findOne(id);
		BankClientAction bankClientAction = new BankClientAction(bankClient, ActionType.DEPOSIT, amount);
		bankClientActionsRepository.save(bankClientAction);
		
		bankClient.setBalance(bankClient.getBalance().add(amount));
		bankClient = bankClientRepository.save(bankClient);
		return new BankClientActionResult(bankClient.getBalance());
	}
	
	@PostMapping("/withdraw")
	@Transactional
	public Result withdraw(@RequestParam(value="id") Long id, @RequestParam(value="amount") BigDecimal amount) {
		BankClient bankClient = bankClientRepository.findOne(id);
		if(bankClient.getBalance().compareTo(amount) == -1){
			return new Result(0, Messages.ERROR_BALANCE_SMALLER_THAN_WITHDRAW);
		}
		BankClientAction bankClientAction = new BankClientAction(bankClient, ActionType.WITHDRAW, amount);
		bankClientActionsRepository.save(bankClientAction);
		
		bankClient.setBalance(bankClient.getBalance().subtract(amount));
		bankClient = bankClientRepository.save(bankClient);
		return new BankClientActionResult(bankClient.getBalance());
	}
	
	@GetMapping("/balanceStatement/{bankClientId}")
	public BalanceStatementResult getBalanceAndStatementByClient(@PathVariable("bankClientId") Long bankClientId) {
	    List<BankClientAction> actions = bankClientActionsRepository.findActionsByClientId(bankClientId);
	    BigDecimal balance = bankClientRepository.findBalanceByClientId(bankClientId);
	    return new BalanceStatementResult(balance, actions);
	}
}
