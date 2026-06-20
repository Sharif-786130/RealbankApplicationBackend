package com.example.BankProject.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.DTO.CreateUserDTO;
import com.example.BankProject.Entity.SystemConfig;
import com.example.BankProject.Entity.User;
import com.example.BankProject.Mapper.UserMapper;
import com.example.BankProject.OfficerDTO.CreateOfficerRequestDTO;
import com.example.BankProject.OfficerDTO.CreateOfficerResponseDTO;
import com.example.BankProject.OfficerDTO.UpdateOfficerRequestDTO;
import com.example.BankProject.OfficerDTO.UpdateOfficerResponseDTO;
import com.example.BankProject.ServiceImple.OfficerServiceImple;
import com.example.BankProject.ServiceImple.SystemConfigService;
import com.example.BankProject.ServiceImple.UserServiceImple;
import com.example.BankProject.UserDTO.UserResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name = "5. Admin — Officers", description = "Admin manages branch officers — hire, update, delete, search")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private final UserServiceImple userService;
	private final OfficerServiceImple officerService;
	private final SystemConfigService configService;
	
	public AdminController(UserServiceImple userService,OfficerServiceImple officerService,SystemConfigService configService) {
		this.userService=userService;
		this.officerService = officerService;
		this.configService=configService;
	}

//	@PostMapping("/users")
//	public UserResponseDTO createUser(@RequestBody CreateUserDTO dto) {
//		return UserMapper .toDTO(userService.createUser(dto));
//	}
//	
//	@PostMapping("/config")
//	public SystemConfig saveConfig(@RequestBody SystemConfig config) {
//		return configService.save(config);
//	}
	
	@Operation(summary = "Hire officer", description = "Admin creates a new officer account.")
	@PostMapping("/officers")
	public ResponseEntity<CreateOfficerResponseDTO> hireOfficer
			(@Valid @RequestBody CreateOfficerRequestDTO Requestdto) {
		
		User officer = officerService.createOfficer(Requestdto);
		
		return ResponseEntity.ok(
				new CreateOfficerResponseDTO(
							"Officer hired successfully",
							 officer.getEmployeeId()
					)
				
			);
	}
	
	@Operation(summary = "Get all officers", description = "Returns all officers in the system.")
	@GetMapping("/getallofficers")
	public List<User> getAllOfficer(){
		return officerService.getAllOfficers();
	}
	
	@Operation(summary = "Get officer by ID")
	@GetMapping("/officers/{id}")
	public User getOfficerById(@PathVariable Long id) {
		return officerService.getOfficerById(id);
	}
	
	@Operation(summary = "Search officer by name")
	@GetMapping("/officers/search")
	public List<User> getOfficerByName(@RequestParam String name){
		return officerService.getOfficerByName(name);
	}
	
	@Operation(summary = "Update officer", description = "Admin updates officer details or status.")
	@PutMapping("/officers/{id}")
	public UpdateOfficerResponseDTO updateOfficer 
					(@PathVariable Long id,
					 @RequestBody UpdateOfficerRequestDTO dto) {
		
		User updatedOfficer = officerService.updateOfficer(id, dto);
		
		return new UpdateOfficerResponseDTO(
				"Officer updated successfully",
				updatedOfficer.getId(),
				updatedOfficer.isActive());
	}
	

	
	@Operation(summary = "Delete officer")
	@DeleteMapping("/officers/{id}")
	public ResponseEntity<?> deleteOfficer(@PathVariable Long id) {
		
		officerService.deleteOfficer(id);
		
		return ResponseEntity.ok(
				Map.of("message","Officer deleted Successfully")
				);
	}
}
