package com.example.BankProject.ServiceImple;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BankProject.AccountDTO.AccountRequestDTO;
import com.example.BankProject.AccountDTO.AccountResponseDTO;
import com.example.BankProject.ENUM.AccountStatus;
import com.example.BankProject.ENUM.AccountType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.MainService.AccountService;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.CustomerRepository;

@Service
public class AccountServiceImpl implements AccountService{
	
	private final AccountRepository accountRepo;
	
	private final CustomerRepository customerRepo;
	
	
	public AccountServiceImpl(AccountRepository accountRepo,CustomerRepository customerRepo) {
		this.accountRepo = accountRepo;
		this.customerRepo = customerRepo;
	}
	
	@Override
	public AccountResponseDTO createAccount(AccountRequestDTO request) {
		
		Customer customer = customerRepo.findById(request.getCustomerId())
				.orElseThrow(() -> new RuntimeException("Customer not Found"));
		
//		if (accountRepo.existsByCustomerCustomerId(request.getCustomerId())) {
//			throw new RuntimeException("Customer already has an account");
//		}
		
		Account account = new Account();
		account.setCustomer(customer);
		account.setAccountType(AccountType.valueOf(request.getAccountType()));
		account.setBalance(request.getInitialDeposit());
		account.setBranchName(request.getBranchName());
		account.setIfscCode(request.getIfscCode());
		account.setAccountNumber(generateAccountNumber());
		
		Account savedAccount = accountRepo.save(account);
		
		return mapToResponse(savedAccount);
		
	}
	
	@Override
	public List<AccountResponseDTO> getAccountByCustomerId(Long customerId){
		
		List<Account> accounts = accountRepo.findByCustomerCustomerId(customerId);
		
		List<AccountResponseDTO> responseList = new ArrayList<>();
		
		for(Account account : accounts) {
			responseList.add(mapToResponse(account));
		}
		
		return responseList;
	}
	
	@Override
	public List<AccountResponseDTO> getAllAccounts(){
		return accountRepo.findAll()
				.stream()
				.map(this::mapToResponse)
				.collect(java.util.stream.Collectors.toList());
		}
	@Override
	public AccountResponseDTO getAccountById(Long accountId) {
		
		Account account = accountRepo.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not Found"));
		
		return mapToResponse(account);
	}
	
	@Override
	public void closeAccount(Long accountId) {
		
		 Account account = accountRepo.findById(accountId)
				 .orElseThrow(() -> new RuntimeException("Account not found"));
		 
		 account.setStatus(AccountStatus.CLOSED);
		 
		 accountRepo.save(account);
	}
	
	private String generateAccountNumber() {
		
		String accountNumber;
		
		do {
			String branchCode = "BNK";
			String year = String.valueOf(LocalDate.now().getYear());
			long count = accountRepo.count() + 1;
			accountNumber = branchCode + year + String.format("%05d", count);
		} while (accountRepo.findByAccountNumber(accountNumber).isPresent());
		
		return accountNumber;		
		
	}
	
	private AccountResponseDTO mapToResponse(Account account) {
		AccountResponseDTO dto = new AccountResponseDTO();
		dto.setAccountId(account.getAccountId());
		dto.setAccountNumber(account.getAccountNumber());
		dto.setAccountType(account.getAccountType().name());
		dto.setBalance(account.getBalance());
		dto.setStatus(account.getStatus().name());
		dto.setBalance(account.getBalance());
		dto.setBranchName(account.getBranchName());
		dto.setIfscCode(account.getIfscCode());
		dto.setCreatedAt(account.getCreatedAt());
		return dto;
	}
	
	

}
