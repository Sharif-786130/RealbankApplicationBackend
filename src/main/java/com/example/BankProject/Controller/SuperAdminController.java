package com.example.BankProject.Controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.BankProject.AdminDTO.CreateAdminRequestDTO;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.User;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.UserRepository;
import com.example.BankProject.ServiceImple.SuperAdminServiceImple;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "10. Super Admin", description = "Super Admin manages admins and views system-wide data")
@RestController
@RequestMapping("/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

	private final SuperAdminServiceImple superAdminService;
	private final UserRepository userRepo;
	private final CustomerRepository customerRepo;

	public SuperAdminController(
			SuperAdminServiceImple superAdminService,
			UserRepository userRepo,
			CustomerRepository customerRepo) {
		this.superAdminService = superAdminService;
		this.userRepo = userRepo;
		this.customerRepo = customerRepo;
	}

	// Create Admin (limited admins)
	@Operation(summary = "Create admin", description = "Super Admin creates a new admin account.")
	@PostMapping
	public String createAdmin(@RequestBody CreateAdminRequestDTO dto) {
		superAdminService.createAdmin(dto);
		return "Admin Created successfully";
	}

	// Get All Admins
	@Operation(summary = "Get all admins")
	@GetMapping("/admins")
	public List<User> getAllAdmins(){
		return superAdminService.getAllAdmins();
	}

	// Get by Id
	@Operation(summary = "Get admin by ID")
	@GetMapping("/admins/{id}")
	public User getAdminById(@PathVariable Long id) {
		return superAdminService.getAdminById(id);
	}

	// Delete by Id
	@Operation(summary = "Delete admin")
	@DeleteMapping("/{id}")
	public String deleteAdmin(@PathVariable Long id) {
		superAdminService.deleteAdmin(id);
		return "Admin deleted successfully";
	}

	// Get All Officers (system-wide, no branch restriction)
	@Operation(summary = "Get all officers (system-wide)", description = "Super Admin views all officers without branch restriction.")
	@GetMapping("/officers")
	public List<User> getAllOfficers() {
		return userRepo.findByRole(Role.OFFICER);
	}

	// Get All Customers (system-wide, no branch restriction)
	@Operation(summary = "Get all customers (system-wide)", description = "Super Admin views all customers without branch restriction.")
	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}
}