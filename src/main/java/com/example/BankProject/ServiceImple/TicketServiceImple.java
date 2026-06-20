package com.example.BankProject.ServiceImple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.BankProject.ENUM.TicketStatus;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.Ticket;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.TicketRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.MainService.TicketService;

@Service
public class TicketServiceImple implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;

    public TicketServiceImple(
            TicketRepository ticketRepository,
            CustomerRepository customerRepository) {

        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Map<String, Object> raiseTicket(
            String subject,
            String description,
            Long customerId,
            CustomUserDetails userDetails) {

        if (subject == null || subject.isBlank()
                || description == null || description.isBlank()) {

            throw new RuntimeException(
                    "Subject and Description are required");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        Ticket ticket = new Ticket();

        ticket.setSubject(subject);
        ticket.setDescription(description);
        ticket.setCustomerId(customerId);

        ticket.setCustomerName(
                customer.getFirstName()
                        + " "
                        + customer.getLastName());

        ticketRepository.save(ticket);

        return Map.of(
                "message", "Ticket raised successfully",
                "ticketNumber", ticket.getTicketNumber());
    }

    @Override
    public List<Ticket> getTicketsByCustomer(Long customerId) {

        return ticketRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public List<Ticket> getOpenTickets() {

        return ticketRepository
                .findByStatusOrderByCreatedAtDesc(TicketStatus.OPEN);
    }

    @Override
    public Map<String, String> resolveTicket(
            Long ticketId,
            String resolution,
            CustomUserDetails userDetails) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        ticket.setStatus(TicketStatus.RESOLVED);

        ticket.setResolution(
                resolution != null && !resolution.isBlank()
                        ? resolution
                        : "Issue resolved by officer");

        ticket.setResolvedByOfficerId(
                userDetails.getId());

        ticket.setResolvedAt(
                LocalDateTime.now());

        ticketRepository.save(ticket);

        return Map.of(
                "message",
                "Ticket resolved successfully");
    }

    @Override
    public List<Ticket> getAllTickets() {

        return ticketRepository
                .findAllByOrderByCreatedAtDesc();
    }
}