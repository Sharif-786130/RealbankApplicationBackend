package com.example.BankProject.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Transaction;
import com.example.BankProject.TransactionDTO.TransactionRequestDTO;

public class TransactionMapper {

    //  Used by StaffController
    public static Transaction toEntity(
            TransactionRequestDTO dto, String user) {
        Transaction tx = new Transaction();
        //  Convert Double to BigDecimal
        tx.setAmount(BigDecimal.valueOf(dto.getAmount()));
        tx.setType(dto.getType());
        //  Use performedById — store as 0 for staff
        tx.setPerformedById(0L);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setStatus("Success");
        return tx;
    }

    //  Used by EmployeeTransactionController
    public static Transaction createTransaction(
            Account account,
            TransactionType type,
            BigDecimal amount,
            BigDecimal balanceBefore,
            BigDecimal balanceAfter,
            Long performedById,
            String description) {

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setBalanceBefore(balanceBefore);
        tx.setBalanceAfter(balanceAfter);
        tx.setPerformedById(performedById);
        tx.setDescription(description);
        tx.setStatus("Success");
        return tx;
    }
}