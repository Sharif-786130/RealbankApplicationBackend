package com.example.BankProject.MainService;

import java.util.List;

import com.example.BankProject.AccountDTO.AccountRequestDTO;
import com.example.BankProject.AccountDTO.AccountResponseDTO;

public interface AccountService {
	
	AccountResponseDTO createAccount(AccountRequestDTO request);
	
	AccountResponseDTO getAccountById(Long accountId);
	
	List<AccountResponseDTO> getAccountByCustomerId(Long customerId);
	
	void closeAccount(Long accountId);
	
	List<AccountResponseDTO> getAllAccounts();
	

}
