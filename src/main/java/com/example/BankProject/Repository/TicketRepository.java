package com.example.BankProject.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BankProject.Entity.Ticket;
import com.example.BankProject.ENUM.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // List versions — used by TicketServiceImple
    List<Ticket> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Ticket> findByStatusOrderByCreatedAtDesc(TicketStatus status);
    List<Ticket> findAllByOrderByCreatedAtDesc();

    // Page versions — used by TicketController directly
    Page<Ticket> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
    Page<Ticket> findByStatusOrderByCreatedAtDesc(TicketStatus status, Pageable pageable);
    Page<Ticket> findAllByOrderByCreatedAtDesc(Pageable pageable);
}