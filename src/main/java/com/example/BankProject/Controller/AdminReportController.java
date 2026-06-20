package com.example.BankProject.Controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.UserRepository;

@RestController
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepo;

    public AdminReportController(
            CustomerRepository customerRepo,
            AccountRepository accountRepo,
            UserRepository userRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    // ✅ NEW — Reports Summary
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {

        long totalCustomers = customerRepo.count();
        long totalAccounts = accountRepo.count();
        long totalOfficers = userRepo.countByRole(Role.OFFICER);
        long totalAdmins = userRepo.countByRole(Role.ADMIN);
        BigDecimal totalBalance = accountRepo.getTotalBalance();

        return ResponseEntity.ok(
            Map.of(
                "totalCustomers", totalCustomers,
                "totalAccounts", totalAccounts,
                "totalOfficers", totalOfficers,
                "totalAdmins", totalAdmins,
                "totalBalance", totalBalance
            )
        );
    }
}
