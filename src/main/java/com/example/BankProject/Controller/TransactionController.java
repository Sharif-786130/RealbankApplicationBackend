//package com.example.BankProject.Controller;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.BankProject.ENUM.AccountStatus;
//import com.example.BankProject.ENUM.TransactionType;
//import com.example.BankProject.Entity.Account;
//import com.example.BankProject.Entity.Transaction;
//import com.example.BankProject.Repository.AccountRepository;
//import com.example.BankProject.Repository.TransactionRepository;
//import com.example.BankProject.Security.CustomUserDetails;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//@Tag(name = "4. Transactions", description = "View transaction history and transfer money between accounts")
//@RestController
//@RequestMapping("/api/transactions")
//@CrossOrigin(origins = "http://localhost:5173")
//public class TransactionController {
//
//	private final AccountRepository accountRepo;
//	private final TransactionRepository transactionRepo;
//
//	public TransactionController(AccountRepository accountRepo, TransactionRepository transactionRepo) {
//		this.accountRepo = accountRepo;
//		this.transactionRepo = transactionRepo;
//	}
//
//	// Helper: map Transaction to response map
//	private Map<String, Object> toMap(Transaction txn) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("transactionId", txn.getTransactionId());
//		map.put("transactionNumber", txn.getTransactionNumber() != null ? txn.getTransactionNumber() : "");
//		map.put("type", txn.getType().name());
//		map.put("amount", txn.getAmount());
//		map.put("balanceBefore", txn.getBalanceBefore());
//		map.put("balanceAfter", txn.getBalanceAfter());
//		map.put("status", txn.getStatus() != null ? txn.getStatus() : "SUCCESS");
//		map.put("description", txn.getDescription() != null ? txn.getDescription() : "");
//		map.put("createdAt", txn.getCreatedAt().toString());
//		return map;
//	}
//
//	// Helper: build paginated response
//	private Map<String, Object> buildPageResponse(Page<Transaction> page) {
//		Map<String, Object> response = new HashMap<>();
//		response.put("data", page.getContent().stream().map(this::toMap).collect(Collectors.toList()));
//		response.put("currentPage", page.getNumber());
//		response.put("totalPages", page.getTotalPages());
//		response.put("totalElements", page.getTotalElements());
//		response.put("pageSize", page.getSize());
//		response.put("isLast", page.isLast());
//		return response;
//	}
//
//	// GET /api/transactions/customer/{customerId}?page=0&size=10
//	@Operation(summary = "Transactions by customer", description = "Returns all transactions across all accounts for a customer.")
//	@GetMapping("/customer/{customerId}")
//	public ResponseEntity<?> getByCustomer(@PathVariable Long customerId, @RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "10") int size) {
//
//		List<Account> accounts = accountRepo.findByCustomerCustomerId(customerId);
//		if (accounts.isEmpty())
//			return ResponseEntity.ok(Map.of("data", List.of(), "totalElements", 0, "totalPages", 0));
//
//		List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
//
//		Pageable pageable = PageRequest.of(page, size);
//		Page<Transaction> txnPage = transactionRepo.findByAccountAccountIdInOrderByCreatedAtDesc(accountIds, pageable);
//
//		return ResponseEntity.ok(buildPageResponse(txnPage));
//	}
//
//	// GET /api/transactions/account/{accountId}?page=0&size=10
//	@Operation(summary = "Transactions by account", description = "Returns transaction history for a specific account.")
//	@GetMapping("/account/{accountId}")
//	public ResponseEntity<?> getByAccount(@PathVariable Long accountId, @RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "10") int size) {
//
//		Pageable pageable = PageRequest.of(page, size);
//		Page<Transaction> txnPage = transactionRepo.findByAccountAccountIdOrderByCreatedAtDesc(accountId, pageable);
//
//		return ResponseEntity.ok(buildPageResponse(txnPage));
//	}
//
//	// POST /api/transactions/transfer (unchanged)
//
//	@Operation(summary = "Transfer money", description = "Customer transfers funds between accounts. Validates ownership, balance, and account status.")
//	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Transfer successful"),
//			@ApiResponse(responseCode = "400", description = "Insufficient balance or invalid account"),
//			@ApiResponse(responseCode = "403", description = "Account does not belong to logged-in customer") })
//	@PostMapping("/transfer")
//	@PreAuthorize("hasRole('CUSTOMER')")
//	public ResponseEntity<?> transfer(@RequestBody Map<String, Object> request,
//			@AuthenticationPrincipal CustomUserDetails userDetails) {
//
//		Long fromAccountId = Long.valueOf(request.get("fromAccountId").toString());
//		String toAccountNumber = request.get("toAccountNumber").toString().trim();
//		BigDecimal amount = new BigDecimal(request.get("amount").toString());
//		String type = request.get("type") != null ? request.get("type").toString() : "TRANSFER";
//
//		if (amount.compareTo(BigDecimal.ZERO) <= 0)
//			return ResponseEntity.badRequest().body(Map.of("message", "Amount must be greater than 0"));
//
//		Account fromAccount = accountRepo.findById(fromAccountId)
//				.orElseThrow(() -> new RuntimeException("Sender account not found"));
//
//		if (!fromAccount.getCustomer().getUserId().equals(userDetails.getId()))
//			return ResponseEntity.status(403)
//					.body(Map.of("message", "Unauthorized: This account does not belong to you"));
//
//		if (fromAccount.getStatus() != AccountStatus.ACTIVE)
//			return ResponseEntity.badRequest().body(Map.of("message", "Your account is not active"));
//
//		if (fromAccount.getBalance().compareTo(amount) < 0)
//			return ResponseEntity.badRequest().body(Map.of("message", "Insufficient balance"));
//
//		Account toAccount = accountRepo.findByAccountNumber(toAccountNumber)
//				.orElseThrow(() -> new RuntimeException("Receiver account not found"));
//
//		if (toAccount.getStatus() != AccountStatus.ACTIVE)
//			return ResponseEntity.badRequest().body(Map.of("message", "Receiver account is not active"));
//
//		if (fromAccount.getAccountId().equals(toAccount.getAccountId()))
//			return ResponseEntity.badRequest().body(Map.of("message", "Cannot transfer to the same account"));
//
//		BigDecimal senderBalanceBefore = fromAccount.getBalance();
//		fromAccount.setBalance(senderBalanceBefore.subtract(amount));
//		accountRepo.save(fromAccount);
//
//		BigDecimal receiverBalanceBefore = toAccount.getBalance();
//		toAccount.setBalance(receiverBalanceBefore.add(amount));
//		accountRepo.save(toAccount);
//
//		Transaction debit = new Transaction();
//		debit.setAccount(fromAccount);
//		debit.setType(TransactionType.TRANSFER);
//		debit.setAmount(amount);
//		debit.setBalanceBefore(senderBalanceBefore);
//		debit.setBalanceAfter(fromAccount.getBalance());
//		debit.setDescription("Transfer to " + toAccountNumber + " via " + type);
//		debit.setPerformedById(userDetails.getId());
//		transactionRepo.save(debit);
//
//		Transaction credit = new Transaction();
//		credit.setAccount(toAccount);
//		credit.setType(TransactionType.DEPOSIT);
//		credit.setAmount(amount);
//		credit.setBalanceBefore(receiverBalanceBefore);
//		credit.setBalanceAfter(toAccount.getBalance());
//		credit.setDescription("Received from " + fromAccount.getAccountNumber() + " via " + type);
//		credit.setPerformedById(userDetails.getId());
//		transactionRepo.save(credit);
//
//		return ResponseEntity.ok(Map.of("message", "Transfer successful", "fromAccount", fromAccount.getAccountNumber(),
//				"toAccount", toAccountNumber, "amount", amount, "newBalance", fromAccount.getBalance()));
//	}
//}



package com.example.BankProject.Controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.ENUM.AccountStatus;
import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Transaction;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.TransactionRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.ServiceImple.EmailServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "4. Transactions", description = "View transaction history and transfer money between accounts")
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

	private final AccountRepository accountRepo;
	private final TransactionRepository transactionRepo;
	private final EmailServiceImpl emailServiceimpl;

	public TransactionController(AccountRepository accountRepo, TransactionRepository transactionRepo,
			EmailServiceImpl emailServiceimpl) {
		this.accountRepo = accountRepo;
		this.transactionRepo = transactionRepo;
		this.emailServiceimpl = emailServiceimpl;
	}

	// Helper: map Transaction to response map
	private Map<String, Object> toMap(Transaction txn) {
		Map<String, Object> map = new HashMap<>();
		map.put("transactionId", txn.getTransactionId());
		map.put("transactionNumber", txn.getTransactionNumber() != null ? txn.getTransactionNumber() : "");
		map.put("type", txn.getType().name());
		map.put("amount", txn.getAmount());
		map.put("balanceBefore", txn.getBalanceBefore());
		map.put("balanceAfter", txn.getBalanceAfter());
		map.put("status", txn.getStatus() != null ? txn.getStatus() : "SUCCESS");
		map.put("description", txn.getDescription() != null ? txn.getDescription() : "");
		map.put("createdAt", txn.getCreatedAt().toString());
		return map;
	}

	// Helper: build paginated response
	private Map<String, Object> buildPageResponse(Page<Transaction> page) {
		Map<String, Object> response = new HashMap<>();
		response.put("data", page.getContent().stream().map(this::toMap).collect(Collectors.toList()));
		response.put("currentPage", page.getNumber());
		response.put("totalPages", page.getTotalPages());
		response.put("totalElements", page.getTotalElements());
		response.put("pageSize", page.getSize());
		response.put("isLast", page.isLast());
		return response;
	}

	// GET /api/transactions/customer/{customerId}?page=0&size=10
	@Operation(summary = "Transactions by customer", description = "Returns all transactions across all accounts for a customer.")
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> getByCustomer(@PathVariable Long customerId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		List<Account> accounts = accountRepo.findByCustomerCustomerId(customerId);
		if (accounts.isEmpty())
			return ResponseEntity.ok(Map.of("data", List.of(), "totalElements", 0, "totalPages", 0));

		List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(page, size);
		Page<Transaction> txnPage = transactionRepo.findByAccountAccountIdInOrderByCreatedAtDesc(accountIds, pageable);

		return ResponseEntity.ok(buildPageResponse(txnPage));
	}

	// GET /api/transactions/account/{accountId}?page=0&size=10
	@Operation(summary = "Transactions by account", description = "Returns transaction history for a specific account.")
	@GetMapping("/account/{accountId}")
	public ResponseEntity<?> getByAccount(@PathVariable Long accountId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Transaction> txnPage = transactionRepo.findByAccountAccountIdOrderByCreatedAtDesc(accountId, pageable);

		return ResponseEntity.ok(buildPageResponse(txnPage));
	}

	// POST /api/transactions/transfer

	@Operation(summary = "Transfer money", description = "Customer transfers funds between accounts. Validates ownership, balance, and account status.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Transfer successful"),
			@ApiResponse(responseCode = "400", description = "Insufficient balance or invalid account"),
			@ApiResponse(responseCode = "403", description = "Account does not belong to logged-in customer") })
	@PostMapping("/transfer")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<?> transfer(@RequestBody Map<String, Object> request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long fromAccountId = Long.valueOf(request.get("fromAccountId").toString());
		String toAccountNumber = request.get("toAccountNumber").toString().trim();
		BigDecimal amount = new BigDecimal(request.get("amount").toString());
		String type = request.get("type") != null ? request.get("type").toString() : "TRANSFER";

		if (amount.compareTo(BigDecimal.ZERO) <= 0)
			return ResponseEntity.badRequest().body(Map.of("message", "Amount must be greater than 0"));

		Account fromAccount = accountRepo.findById(fromAccountId)
				.orElseThrow(() -> new RuntimeException("Sender account not found"));

		if (!fromAccount.getCustomer().getUserId().equals(userDetails.getId()))
			return ResponseEntity.status(403)
					.body(Map.of("message", "Unauthorized: This account does not belong to you"));

		if (fromAccount.getStatus() != AccountStatus.ACTIVE)
			return ResponseEntity.badRequest().body(Map.of("message", "Your account is not active"));

		if (fromAccount.getBalance().compareTo(amount) < 0)
			return ResponseEntity.badRequest().body(Map.of("message", "Insufficient balance"));

		Account toAccount = accountRepo.findByAccountNumber(toAccountNumber)
				.orElseThrow(() -> new RuntimeException("Receiver account not found"));

		if (toAccount.getStatus() != AccountStatus.ACTIVE)
			return ResponseEntity.badRequest().body(Map.of("message", "Receiver account is not active"));

		if (fromAccount.getAccountId().equals(toAccount.getAccountId()))
			return ResponseEntity.badRequest().body(Map.of("message", "Cannot transfer to the same account"));

		BigDecimal senderBalanceBefore = fromAccount.getBalance();
		fromAccount.setBalance(senderBalanceBefore.subtract(amount));
		accountRepo.save(fromAccount);

		BigDecimal receiverBalanceBefore = toAccount.getBalance();
		toAccount.setBalance(receiverBalanceBefore.add(amount));
		accountRepo.save(toAccount);

		Transaction debit = new Transaction();
		debit.setAccount(fromAccount);
		debit.setType(TransactionType.TRANSFER);
		debit.setAmount(amount);
		debit.setBalanceBefore(senderBalanceBefore);
		debit.setBalanceAfter(fromAccount.getBalance());
		debit.setDescription("Transfer to " + toAccountNumber + " via " + type);
		debit.setPerformedById(userDetails.getId());
		transactionRepo.save(debit);

		Transaction credit = new Transaction();
		credit.setAccount(toAccount);
		credit.setType(TransactionType.DEPOSIT);
		credit.setAmount(amount);
		credit.setBalanceBefore(receiverBalanceBefore);
		credit.setBalanceAfter(toAccount.getBalance());
		credit.setDescription("Received from " + fromAccount.getAccountNumber() + " via " + type);
		credit.setPerformedById(userDetails.getId());
		transactionRepo.save(credit);

//		// ── send email to SENDER ──
//				try {
//					emailServiceimpl.sendTransactionEmail(
//							fromAccount.getCustomer().getEmail(),
//							"TRANSFER (Debit)",
//							amount.doubleValue(),
//							fromAccount.getAccountNumber(),
//							fromAccount.getBalance().doubleValue()
//					);
//				} catch (Exception e) {
//					System.err.println("Failed to send sender email: " + e.getMessage());
//				}
		
		System.out.println("DEBUG: About to send sender email to: " + fromAccount.getCustomer().getEmail());
		try {
			emailServiceimpl.sendTransactionEmail(
					fromAccount.getCustomer().getEmail(),
					"TRANSFER (Debit)",
					amount.doubleValue(),
					fromAccount.getAccountNumber(),
					fromAccount.getBalance().doubleValue()
			);
			System.out.println("DEBUG: Sender email sent successfully (no exception thrown)");
		} catch (Exception e) {
			System.err.println("Failed to send sender email: " + e.getMessage());
			e.printStackTrace();
		}

				// ── send email to RECEIVER ──
				try {
					emailServiceimpl.sendTransactionEmail(
							toAccount.getCustomer().getEmail(),
							"TRANSFER (Credit)",
							amount.doubleValue(),
							toAccount.getAccountNumber(),
							toAccount.getBalance().doubleValue()
					);
				} catch (Exception e) {
					System.err.println("Failed to send receiver email: " + e.getMessage());
				}

				return ResponseEntity.ok(Map.of("message", "Transfer successful", "fromAccount", fromAccount.getAccountNumber(),
						"toAccount", toAccountNumber, "amount", amount, "newBalance", fromAccount.getBalance()));
	}
}