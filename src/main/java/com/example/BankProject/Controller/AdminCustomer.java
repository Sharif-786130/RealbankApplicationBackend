package com.example.BankProject.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.ENUM.KycStatus;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Repository.CustomerRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "7. Admin — Customers", description = "Admin manages customer KYC and views all customers")
@RestController
@RequestMapping("/admin/customers")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminCustomer {
	
	private final CustomerRepository customerRepo;
	
	public AdminCustomer(CustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	
	@Operation(summary = "Update KYC status", description = "Admin verifies or rejects a customer's KYC documents.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "KYC status updated"),
	    @ApiResponse(responseCode = "400", description = "Invalid status")
	})
	@PutMapping("/{id}/kyc")
	public ResponseEntity<Customer>verifyKyc(
			@PathVariable Long id,
			@RequestParam KycStatus status){
	
		Customer customer =customerRepo
				.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer are not Found"));
		
		customer.setKycStatus(status);
		return ResponseEntity.ok(customerRepo.save(customer));
}
	
	   // ✅ NEW — View All Customers
	@Operation(summary = "Get all customers", description = "Admin views all customers system-wide.")
	@GetMapping
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerRepo.findAll());
    }

    // ✅ NEW — Update Customer Status
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<?> updateCustomerStatus(
            @PathVariable Long customerId,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        Customer customer = customerRepo
                .findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // ACTIVE → isActive = true
        // SUSPENDED or BLOCKED → isActive = false
        if (status.equals("ACTIVE")) {
            customer.setIsActive(true);
        } else {
            customer.setIsActive(false);
        }

        customerRepo.save(customer);

        return ResponseEntity.ok(
            Map.of(
                "message", "Customer status updated successfully",
                "customerId", customerId,
                "status", status
            )
        );
    }
}
