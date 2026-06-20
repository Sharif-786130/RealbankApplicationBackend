package com.example.BankProject.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Transaction;
import com.example.BankProject.Repository.TransactionRepository;

@RestController
@RequestMapping("/admin/transactions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTransactionController {

    private final TransactionRepository transactionRepo;

    public AdminTransactionController(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    // GET /admin/transactions?type=TRANSFER&fromDate=2024-01-01&toDate=2024-12-31&page=0&size=10
    @GetMapping
    public ResponseEntity<?> getTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay()   : null;
        LocalDateTime to   = toDate   != null ? toDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> result = transactionRepo.findByFilters(type, from, to, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data",          result.getContent());
        response.put("currentPage",   result.getNumber());
        response.put("totalPages",    result.getTotalPages());
        response.put("totalElements", result.getTotalElements());
        response.put("isLast",        result.isLast());
        return ResponseEntity.ok(response);
    }
}