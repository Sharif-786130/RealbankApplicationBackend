package com.example.BankProject.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // List versions — used by TransactionServiceImple
    List<Transaction> findByAccountAccountId(Long accountId);
    List<Transaction> findByAccountCustomerCustomerId(Long customerId);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByAccountAccountIdOrderByCreatedAtDesc(Long accountId);
    List<Transaction> findByAccountAccountIdInOrderByCreatedAtDesc(List<Long> accountIds);

    // Page versions — used by TransactionController directly
    Page<Transaction> findByAccountAccountIdOrderByCreatedAtDesc(Long accountId, Pageable pageable);
    Page<Transaction> findByAccountAccountIdInOrderByCreatedAtDesc(List<Long> accountIds, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
           "(:type IS NULL OR t.Type = :type) AND " +
           "(:fromDate IS NULL OR t.createdAt >= :fromDate) AND " +
           "(:toDate IS NULL OR t.createdAt <= :toDate) " +
           "ORDER BY t.createdAt DESC")
    Page<Transaction> findByFilters(
            @Param("type") TransactionType type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);
}