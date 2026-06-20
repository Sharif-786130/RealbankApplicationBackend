package com.example.BankProject.Controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.LoanDTO.LoanRequestDTO;
import com.example.BankProject.LoanDTO.LoanResponseDTO;
import com.example.BankProject.MainService.LoanService;
import com.example.BankProject.Security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3. Loans", description = "Apply, approve, reject and manage customer loans")
@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // GET /api/loans?page=0&size=10
    @Operation(summary = "Get all loans", description = "Admin/Officer/SuperAdmin view of all loans.")
    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllLoans(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LoanResponseDTO> result = loanService.getAllLoans(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data",          result.getContent());
        response.put("currentPage",   result.getNumber());
        response.put("totalPages",    result.getTotalPages());
        response.put("totalElements", result.getTotalElements());
        response.put("isLast",        result.isLast());
        return ResponseEntity.ok(response);
    }

    // GET /api/loans/{loanId}
    @Operation(summary = "Get loan by ID")
    @GetMapping("/{loanId}")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER', 'SUPER_ADMIN')")
    public LoanResponseDTO getLoan(@PathVariable Long loanId) {
        return loanService.getLoanById(loanId);
    }

    // GET /api/loans/customer/{customerId}?page=0&size=10
    @Operation(summary = "Get loans by customer", description = "Returns all loans for a specific customer.")
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'CUSTOMER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getLoansByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LoanResponseDTO> result = loanService.getLoanByCustomer(customerId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data",          result.getContent());
        response.put("currentPage",   result.getNumber());
        response.put("totalPages",    result.getTotalPages());
        response.put("totalElements", result.getTotalElements());
        response.put("isLast",        result.isLast());
        return ResponseEntity.ok(response);
    }

    // POST /api/loans
    @Operation(summary = "Create loan application", description = "Officer or Customer submits a new loan application.")
    @PostMapping
    @PreAuthorize("hasAnyRole('OFFICER', 'CUSTOMER')")
    public LoanResponseDTO createLoan(
            @RequestBody LoanRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return loanService.createLoan(request, userDetails);
    }

    // PUT /api/loans/{loanId}/approve
    @Operation(summary = "Approve loan", description = "Officer approves loans ≤ ₹1,00,000. Admin approves loans up to ₹5,00,000.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loan approved"),
        @ApiResponse(responseCode = "403", description = "Loan exceeds your approval limit")
    })
    @PutMapping("/{loanId}/approve")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    public LoanResponseDTO approveLoan(
            @PathVariable Long loanId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return loanService.approveLoan(loanId, userDetails);
    }

    // PUT /api/loans/{loanId}/reject
    @Operation(summary = "Reject loan")
    @PutMapping("/{loanId}/reject")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    public LoanResponseDTO rejectLoan(@PathVariable Long loanId) {
        return loanService.rejectLoan(loanId);
    }

    // POST /api/loans/{loanId}/pay
    @Operation(summary = "Pay EMI", description = "Customer or Officer processes an EMI payment.")
    @PostMapping("/{loanId}/pay")
    @PreAuthorize("hasAnyRole('OFFICER', 'CUSTOMER')")
    public LoanResponseDTO payEmi(
            @PathVariable Long loanId,
            @RequestParam BigDecimal amount) {
        return loanService.payEmi(loanId, amount);
    }

    // PUT /api/loans/{loanId}/close
    @Operation(summary = "Close loan", description = "Officer or Admin closes a fully paid loan.")
    @PutMapping("/{loanId}/close")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    public LoanResponseDTO closeLoan(@PathVariable Long loanId) {
        return loanService.closeLoan(loanId);
    }
}