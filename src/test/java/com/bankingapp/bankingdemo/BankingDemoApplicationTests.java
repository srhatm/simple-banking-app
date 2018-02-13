package com.bankingapp.bankingdemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bankingapp.bankingdemo.model.BankClient;
import com.bankingapp.bankingdemo.service.response.BalanceStatementResult;
import com.bankingapp.bankingdemo.service.response.BankClientActionResult;
import com.bankingapp.bankingdemo.service.response.LoginResult;
import com.bankingapp.bankingdemo.service.response.Result;
import com.bankingapp.bankingdemo.util.Messages;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql({ "classpath:init-data.sql" })
public class BankingDemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private static final String URL = "/rest/banking/";

	@Test
	public void contextLoads() {
	}

	// createClient test
	@Test
	public void testAddClient() throws Exception {

		BankClient client = new BankClient("Robin", "Hood", "robin.hood@gmail.com", "1234");

		ResponseEntity<BankClient> responseEntity = (ResponseEntity<BankClient>) restTemplate.postForEntity(URL + "add",
				client, BankClient.class);

		int status = responseEntity.getStatusCodeValue();
		BankClient resultClient = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(resultClient);
		assertNotNull(resultClient.getId());

	}

	// invalid login test: password is incorrect
	@Test
	public void testClientInvalidLogin() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("email", "peter.pan@yahoo.com");
		map.add("password", "11111");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<LoginResult> responseEntity = (ResponseEntity<LoginResult>) restTemplate.postForEntity(URL + "login",
				request, LoginResult.class);

		int status = responseEntity.getStatusCodeValue();
		Result result = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getSuccess(), 0);
		assertEquals(result.getMessage(), Messages.ERROR_INVALID_LOGIN);
	}

	// login test
	@Test
	public void testClientLogin() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("email", "peter.pan@yahoo.com");
		map.add("password", "1111");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<LoginResult> responseEntity = (ResponseEntity<LoginResult>) restTemplate.postForEntity(URL + "login",
				request, LoginResult.class);

		int status = responseEntity.getStatusCodeValue();
		LoginResult result = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getId().longValue(), 1L);
		assertEquals(result.getSuccess(), 1);
	}

	// withdraw test - withdraw amount is greater than amount
	@Test
	public void testClientWithdrawGreaterThanBalance() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("id", "1");
		map.add("amount", "2000");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<Result> responseEntity = (ResponseEntity<Result>) restTemplate.postForEntity(URL + "withdraw",
				request, Result.class);

		int status = responseEntity.getStatusCodeValue();
		Result result = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getSuccess(), 0);
		assertEquals(result.getMessage(), Messages.ERROR_BALANCE_SMALLER_THAN_WITHDRAW);

	}

	// withdraw test - withdraw amount is less than amount
	@Test
	public void testClientWithdraw() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("id", "1");
		map.add("amount", "500");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<BankClientActionResult> responseEntity = (ResponseEntity<BankClientActionResult>) restTemplate.postForEntity(URL + "withdraw",
				request, BankClientActionResult.class);

		int status = responseEntity.getStatusCodeValue();
		BankClientActionResult result = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getSuccess(), 1);
		assertEquals(result.getBalance().intValue(), 1000);

	}

	// deposit test
	@Test
	public void testClientDeposit() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("id", "1");
		map.add("amount", "1500");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<BankClientActionResult> responseEntity = (ResponseEntity<BankClientActionResult>) restTemplate.postForEntity(URL + "deposit",
				request, BankClientActionResult.class);

		int status = responseEntity.getStatusCodeValue();
		BankClientActionResult result = responseEntity.getBody();
		
		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getSuccess(), 1);
		assertEquals(result.getBalance().intValue(), 3000);

	}

	// balance and statement test
	@Test
	public void testGetBalanceAndStatement() throws Exception {

		ResponseEntity<BalanceStatementResult> responseEntity = restTemplate
				.getForEntity(URL + "balanceStatement/{bankClientId}", BalanceStatementResult.class, new Long(1));

		int status = responseEntity.getStatusCodeValue();
		BalanceStatementResult result = responseEntity.getBody();

		assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
		assertNotNull(result);
		assertEquals(result.getBalance().intValue(), 1500);
		assertEquals(result.getActions().size(), 2);
	}

}
