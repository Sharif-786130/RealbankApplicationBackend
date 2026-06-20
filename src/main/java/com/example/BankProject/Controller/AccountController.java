//package com.example.BankProject.Controller;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//import com.example.BankProject.AccountDTO.AccountRequestDTO;
//import com.example.BankProject.AccountDTO.AccountResponseDTO;
//import com.example.BankProject.ENUM.AccountStatus;
//import com.example.BankProject.ENUM.TransactionType;
//import com.example.BankProject.Entity.Account;
//import com.example.BankProject.Entity.Transaction;
//import com.example.BankProject.Repository.AccountRepository;
//import com.example.BankProject.Repository.TransactionRepository;
//import com.example.BankProject.Security.CustomUserDetails;
//import com.example.BankProject.ServiceImple.AccountServiceImpl;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//@Tag(name = "2. Accounts", description = "Create accounts, get balances, teller deposit and withdrawal")
//@RestController
//@RequestMapping("/api/account")
//@CrossOrigin(origins = "http://localhost:5173")
//public class AccountController {
//
//    private final AccountServiceImpl accountService;
//    private final TransactionRepository transactionRepo;
//    private final AccountRepository accountRepo;
//
//    public AccountController(AccountServiceImpl accountService,
//            TransactionRepository transactionRepo,
//            AccountRepository accountRepo) {
//        this.accountService = accountService;
//        this.transactionRepo = transactionRepo;
//        this.accountRepo = accountRepo;
//    }
//
//    // Officer only — create account
//    @Operation(summary = "Create account", description = "Officer opens a new bank account for a customer.")
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('OFFICER')")
//    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO request) {
//        return accountService.createAccount(request);
//    }
//
//    // Admin, Officer, Super Admin — get all accounts
//    @Operation(summary = "Get all accounts", description = "Admin/Officer view of all accounts system-wide.")
//    @GetMapping("")
//    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'SUPER_ADMIN')")
//    public List<AccountResponseDTO> getAllAccounts() {
//        return accountService.getAllAccounts();
//    }
//
//    // Officer, Admin, Customer — get account by id
//    @Operation(summary = "Get account by ID", description = "Fetch a single account by its ID.")
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
//    public AccountResponseDTO getAccount(@PathVariable Long id) {
//        return accountService.getAccountById(id);
//    }
//
//    // Officer, Admin, Customer — get accounts by customer
//    @Operation(summary = "Get accounts by customer", description = "Returns all accounts for a specific customer.")
//    @GetMapping("/customer/{customerId}")
//    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
//    public List<AccountResponseDTO> getAccountsByCustomer(
//            @PathVariable Long customerId) {
//        return accountService.getAccountByCustomerId(customerId);
//    }
//
//    // Officer, Admin, Customer — get transactions by account
//    @Operation(summary = "Get transactions by account", description = "Returns transaction history for a specific account.")
//    @GetMapping("/{accountId}/transactions")
//    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
//    public List<Transaction> getTransactionsByAccount(
//            @PathVariable Long accountId) {
//        return transactionRepo.findByAccountAccountId(accountId);
//    }
//
//    // Officer, Admin, Customer — get transactions by customer
//    @Operation(summary = "Get transactions by customer", description = "Returns all transactions across all of a customer's accounts.")
//    @GetMapping("/customer/{customerId}/transactions")
//    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
//    public List<Transaction> getTransactionsByCustomer(
//            @PathVariable Long customerId) {
//        return transactionRepo.findByAccountCustomerCustomerId(customerId);
//    }
//    
// // Officer — cash deposit (teller)
//    @Operation(summary = "Cash deposit (Teller)", description = "Officer deposits cash into a customer account at the branch.")
//    @ApiResponses({
//        @ApiResponse(responseCode = "200", description = "Deposit successful"),
//        @ApiResponse(responseCode = "400", description = "Account not active or invalid amount")
//    })
//    @PostMapping("/{accountId}/deposit")
//    @PreAuthorize("hasRole('OFFICER')")
//    public ResponseEntity<?> deposit(
//            @PathVariable Long accountId,
//            @RequestBody Map<String, Object> request,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        BigDecimal amount = new BigDecimal(request.get("amount").toString());
//        String description = request.getOrDefault("description", "Cash Deposit").toString();
//
//        if (amount.compareTo(BigDecimal.ZERO) <= 0)
//            return ResponseEntity.badRequest()
//                .body(Map.of("message", "Amount must be greater than 0"));
//
//        Account account = accountRepo.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        if (account.getStatus() != AccountStatus.ACTIVE)
//            return ResponseEntity.badRequest()
//                .body(Map.of("message", "Account is not active"));
//
//        BigDecimal before = account.getBalance();
//        account.setBalance(before.add(amount));
//        accountRepo.save(account);
//
//        Transaction txn = new Transaction();
//        txn.setAccount(account);
//        txn.setType(TransactionType.DEPOSIT);
//        txn.setAmount(amount);
//        txn.setBalanceBefore(before);
//        txn.setBalanceAfter(account.getBalance());
//        txn.setDescription(description);
//        txn.setPerformedById(userDetails.getId());
//        transactionRepo.save(txn);
//
//        return ResponseEntity.ok(Map.of(
//            "message",    "Deposit successful",
//            "newBalance", account.getBalance(),
//            "amount",     amount
//        ));
//    }
//
//    // Officer — cash withdrawal (teller)
//    @Operation(summary = "Cash withdrawal (Teller)", description = "Officer processes a cash withdrawal for a walk-in customer.")
//    @ApiResponses({
//        @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
//        @ApiResponse(responseCode = "400", description = "Insufficient balance or account not active")
//    })
//    @PostMapping("/{accountId}/withdraw")
//    @PreAuthorize("hasRole('OFFICER')")
//    public ResponseEntity<?> withdraw(
//            @PathVariable Long accountId,
//            @RequestBody Map<String, Object> request,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        BigDecimal amount = new BigDecimal(request.get("amount").toString());
//        String description = request.getOrDefault("description", "Cash Withdrawal").toString();
//
//        if (amount.compareTo(BigDecimal.ZERO) <= 0)
//            return ResponseEntity.badRequest()
//                .body(Map.of("message", "Amount must be greater than 0"));
//
//        Account account = accountRepo.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        if (account.getStatus() != AccountStatus.ACTIVE)
//            return ResponseEntity.badRequest()
//                .body(Map.of("message", "Account is not active"));
//
//        if (account.getBalance().compareTo(amount) < 0)
//            return ResponseEntity.badRequest()
//                .body(Map.of("message", "Insufficient balance"));
//
//        BigDecimal before = account.getBalance();
//        account.setBalance(before.subtract(amount));
//        accountRepo.save(account);
//
//        Transaction txn = new Transaction();
//        txn.setAccount(account);
//        txn.setType(TransactionType.WITHDRAW);
//        txn.setAmount(amount);
//        txn.setBalanceBefore(before);
//        txn.setBalanceAfter(account.getBalance());
//        txn.setDescription(description);
//        txn.setPerformedById(userDetails.getId());
//        transactionRepo.save(txn);
//
//        return ResponseEntity.ok(Map.of(
//            "message",    "Withdrawal successful",
//            "newBalance", account.getBalance(),
//            "amount",     amount
//        ));
//    }
//} 




package com.example.BankProject.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.BankProject.AccountDTO.AccountRequestDTO;
import com.example.BankProject.AccountDTO.AccountResponseDTO;
import com.example.BankProject.ENUM.AccountStatus;
import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Transaction;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.TransactionRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.ServiceImple.AccountServiceImpl;
import com.example.BankProject.ServiceImple.EmailServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2. Accounts", description = "Create accounts, get balances, teller deposit and withdrawal")
@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final EmailServiceImpl emailServiceimpl;

    public AccountController(AccountServiceImpl accountService,
            TransactionRepository transactionRepo,
            AccountRepository accountRepo,
            EmailServiceImpl emailServiceimpl) {
        this.accountService = accountService;
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.emailServiceimpl = emailServiceimpl;
    }

    // Officer only — create account
    @Operation(summary = "Create account", description = "Officer opens a new bank account for a customer.")
    @PostMapping("/create")
    @PreAuthorize("hasRole('OFFICER')")
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO request) {
        return accountService.createAccount(request);
    }

    // Admin, Officer, Super Admin — get all accounts
    @Operation(summary = "Get all accounts", description = "Admin/Officer view of all accounts system-wide.")
    @GetMapping("")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'SUPER_ADMIN')")
    public List<AccountResponseDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // Officer, Admin, Customer — get account by id
    @Operation(summary = "Get account by ID", description = "Fetch a single account by its ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
    public AccountResponseDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    // Officer, Admin, Customer — get accounts by customer
    @Operation(summary = "Get accounts by customer", description = "Returns all accounts for a specific customer.")
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
    public List<AccountResponseDTO> getAccountsByCustomer(
            @PathVariable Long customerId) {
        return accountService.getAccountByCustomerId(customerId);
    }

    // Officer, Admin, Customer — get transactions by account
    @Operation(summary = "Get transactions by account", description = "Returns transaction history for a specific account.")
    @GetMapping("/{accountId}/transactions")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
    public List<Transaction> getTransactionsByAccount(
            @PathVariable Long accountId) {
        return transactionRepo.findByAccountAccountId(accountId);
    }

    // Officer, Admin, Customer — get transactions by customer
    @Operation(summary = "Get transactions by customer", description = "Returns all transactions across all of a customer's accounts.")
    @GetMapping("/customer/{customerId}/transactions")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER')")
    public List<Transaction> getTransactionsByCustomer(
            @PathVariable Long customerId) {
        return transactionRepo.findByAccountCustomerCustomerId(customerId);
    }
    
 // Officer — cash deposit (teller)
    @Operation(summary = "Cash deposit (Teller)", description = "Officer deposits cash into a customer account at the branch.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deposit successful"),
        @ApiResponse(responseCode = "400", description = "Account not active or invalid amount")
    })
    @PostMapping("/{accountId}/deposit")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<?> deposit(
            @PathVariable Long accountId,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String description = request.getOrDefault("description", "Cash Deposit").toString();

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Amount must be greater than 0"));

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE)
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Account is not active"));

        BigDecimal before = account.getBalance();
        account.setBalance(before.add(amount));
        accountRepo.save(account);

        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setType(TransactionType.DEPOSIT);
        txn.setAmount(amount);
        txn.setBalanceBefore(before);
        txn.setBalanceAfter(account.getBalance());
        txn.setDescription(description);
        txn.setPerformedById(userDetails.getId());
        transactionRepo.save(txn);

        // ── send email to customer ──
        emailServiceimpl.sendTransactionEmail(
                account.getCustomer().getEmail(),
                "DEPOSIT",
                amount.doubleValue(),
                account.getAccountNumber(),
                account.getBalance().doubleValue()
        );

        return ResponseEntity.ok(Map.of(
            "message",    "Deposit successful",
            "newBalance", account.getBalance(),
            "amount",     amount
        ));
    }

    // Officer — cash withdrawal (teller)
    @Operation(summary = "Cash withdrawal (Teller)", description = "Officer processes a cash withdrawal for a walk-in customer.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
        @ApiResponse(responseCode = "400", description = "Insufficient balance or account not active")
    })
    @PostMapping("/{accountId}/withdraw")
    @PreAuthorize("hasRole('OFFICER')")
    public ResponseEntity<?> withdraw(
            @PathVariable Long accountId,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String description = request.getOrDefault("description", "Cash Withdrawal").toString();

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Amount must be greater than 0"));

        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE)
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Account is not active"));

        if (account.getBalance().compareTo(amount) < 0)
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Insufficient balance"));

        BigDecimal before = account.getBalance();
        account.setBalance(before.subtract(amount));
        accountRepo.save(account);

        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setType(TransactionType.WITHDRAW);
        txn.setAmount(amount);
        txn.setBalanceBefore(before);
        txn.setBalanceAfter(account.getBalance());
        txn.setDescription(description);
        txn.setPerformedById(userDetails.getId());
        transactionRepo.save(txn);

        // ── send email to customer ──
        emailServiceimpl.sendTransactionEmail(
                account.getCustomer().getEmail(),
                "WITHDRAWAL",
                amount.doubleValue(),
                account.getAccountNumber(),
                account.getBalance().doubleValue()
        );

        return ResponseEntity.ok(Map.of(
            "message",    "Withdrawal successful",
            "newBalance", account.getBalance(),
            "amount",     amount
        ));
    }
}