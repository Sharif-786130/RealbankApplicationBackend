package com.example.BankProject.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.CustomerDTO.ResetPasswordRequest;
import com.example.BankProject.MainService.CustomerService;
import com.example.BankProject.Security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "9. Customer", description = "Customer self-service — reset password")
@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {
	
	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
		
	}
	
	@Operation(summary = "Reset password", description = "Customer resets their own password. Required on first login.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Password updated"),
	    @ApiResponse(responseCode = "403", description = "Unauthorized")
	})
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
			@RequestBody ResetPasswordRequest request,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		customerService.resetPassword(
				userDetails.getId(),
				request.getNewPassword());
		
		return ResponseEntity.ok(
				java.util.Map.of("message","Password updated successfully")
			);
	}

}
