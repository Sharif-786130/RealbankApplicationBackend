package com.example.BankProject.MainService;

import java.util.List;
import java.util.Map;

import com.example.BankProject.Entity.Ticket;
import com.example.BankProject.Security.CustomUserDetails;

public interface TicketService {

    Map<String, Object> raiseTicket(
            String subject,
            String description,
            Long customerId,
            CustomUserDetails userDetails);

    List<Ticket> getTicketsByCustomer(Long customerId);

    List<Ticket> getOpenTickets();

    Map<String, String> resolveTicket(
            Long ticketId,
            String resolution,
            CustomUserDetails userDetails);

    List<Ticket> getAllTickets();
}