package com.example.BankProject.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.ENUM.TicketStatus;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.Ticket;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.TicketRepository;
import com.example.BankProject.Security.CustomUserDetails;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    private final TicketRepository ticketRepo;
    private final CustomerRepository customerRepo;

    public TicketController(TicketRepository ticketRepo, CustomerRepository customerRepo) {
        this.ticketRepo = ticketRepo;
        this.customerRepo = customerRepo;
    }

    // POST /api/tickets/raise (unchanged)
    @PostMapping("/raise")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> raiseTicket(@RequestBody Map<String, String> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String subject     = request.get("subject");
        String description = request.get("description");
        Long customerId    = Long.parseLong(request.get("customerId"));

        if (subject == null || description == null || subject.isBlank() || description.isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Subject and description are required"));

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(subject);
        ticket.setDescription(description);
        ticket.setCustomerId(customerId);
        ticket.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        ticketRepo.save(ticket);

        return ResponseEntity.ok(Map.of(
                "message", "Ticket raised successfully",
                "ticketNumber", ticket.getTicketNumber()));
    }

    // GET /api/tickets/customer/{customerId}?page=0&size=10
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','OFFICER','ADMIN')")
    public ResponseEntity<?> getMyTickets(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> result = ticketRepo.findByCustomerIdOrderByCreatedAtDesc(customerId, pageable);
        return ResponseEntity.ok(result);
    }

    // GET /api/tickets/open?page=0&size=10
    @GetMapping("/open")
    @PreAuthorize("hasAnyRole('OFFICER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> getOpenTickets(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> result = ticketRepo.findByStatusOrderByCreatedAtDesc(TicketStatus.OPEN, pageable);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/tickets/{ticketId}/resolve (unchanged)
    @PatchMapping("/{ticketId}/resolve")
    @PreAuthorize("hasAnyRole('OFFICER','ADMIN')")
    public ResponseEntity<?> resolveTicket(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setResolution(request.getOrDefault("resolution", "Issue resolved by officer"));
        ticket.setResolvedByOfficerId(userDetails.getId());
        ticket.setResolvedAt(LocalDateTime.now());
        ticketRepo.save(ticket);

        return ResponseEntity.ok(Map.of("message", "Ticket resolved successfully"));
    }

    // GET /api/tickets/all?page=0&size=10
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> getAllTickets(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> result = ticketRepo.findAllByOrderByCreatedAtDesc(pageable);
        return ResponseEntity.ok(result);
    }
}