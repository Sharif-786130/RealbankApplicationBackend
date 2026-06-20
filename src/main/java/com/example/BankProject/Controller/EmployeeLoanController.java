package com.example.BankProject.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.ENUM.LoanStatus;
import com.example.BankProject.Entity.Loan;
import com.example.BankProject.Repository.LoanRepository;
import com.example.BankProject.Security.CustomUserDetails;

@RestController
@RequestMapping("/employee/loans")
@PreAuthorize("hasRole('OFFICER')")
public class EmployeeLoanController {

    //  Only LoanRepository needed
    private final LoanRepository loanRepo;

    public EmployeeLoanController(LoanRepository loanRepo) {
        this.loanRepo = loanRepo;
    }

    //  LOAN VERIFICATION
    @PutMapping("/{loanId}/verify")
    public ResponseEntity<?> verifyLoan(
            @PathVariable Long loanId,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Boolean verified = (Boolean) request.get("verified");
        String remarks = (String) request.get("remarks");

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (Boolean.TRUE.equals(verified)) {
            loan.setStatus(LoanStatus.VERIFIED);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
        }

        loanRepo.save(loan);

        return ResponseEntity.ok(Map.of(
            "loanId", loanId,
            "status", loan.getStatus(),
            "remarks", remarks != null ? remarks : "",
            "message", "Loan verification updated"
        ));
    }

    //  LOAN APPROVAL / REJECTION
    @PatchMapping("/{loanId}/status")
    public ResponseEntity<?> updateLoanStatus(
            @PathVariable Long loanId,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String status = request.get("status");
        String remarks = request.get("remarks");

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.VERIFIED) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "message",
                    "Loan must be verified first before approval or rejection"
                ));
        }

        if (status.equals("APPROVED")) {
            loan.setStatus(LoanStatus.APPROVED);
        } else if (status.equals("REJECTED")) {
            loan.setStatus(LoanStatus.REJECTED);
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Status must be APPROVED or REJECTED"));
        }

        loanRepo.save(loan);

        return ResponseEntity.ok(Map.of(
            "loanId", loanId,
            "status", loan.getStatus(),
            "remarks", remarks != null ? remarks : "",
            "message", "Loan status updated successfully"
        ));
    }
}