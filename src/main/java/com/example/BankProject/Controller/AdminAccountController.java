package com.example.BankProject.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.ENUM.AccountStatus;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.ServiceImple.AccountServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "6. Admin — Accounts", description = "Admin views and manages account statuses — freeze, block, activate")
@RestController
@RequestMapping("/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminAccountController {

    private final AccountRepository accountRepo;
    private final AccountServiceImpl accountService;

    public AdminAccountController(AccountRepository accountRepo,
                                   AccountServiceImpl accountService) {
        this.accountRepo    = accountRepo;
        this.accountService = accountService;
    }

    // ✅ Get all accounts — uses service to return safe DTOs
    @Operation(summary = "Get all accounts", description = "Admin view of every account in the system.")
    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // ✅ Freeze / Activate / Block account
    @Operation(summary = "Update account status", description = "Admin freezes, blocks, or activates a customer account.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated"),
        @ApiResponse(responseCode = "400", description = "Invalid status value or account not found")
    })
    @PatchMapping("/{accountId}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable Long accountId,
            @RequestBody StatusRequest request) {

        String status = request.status;

        Account account = accountRepo
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (status.equals("FROZEN")) {
            account.setStatus(AccountStatus.FROZEN);
        } else if (status.equals("ACTIVE")) {
            account.setStatus(AccountStatus.ACTIVE);
        } else if (status.equals("BLOCKED")) {
            account.setStatus(AccountStatus.BLOCKED);
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of("message",
                    "Invalid status. Use FROZEN, ACTIVE or BLOCKED"));
        }

        accountRepo.save(account);

        return ResponseEntity.ok(Map.of(
            "message",   "Account status updated successfully",
            "accountId", accountId,
            "status",    status
        ));
    }

    // Inner class to receive status from request body
    static class StatusRequest {
        public String status;
    }
}
