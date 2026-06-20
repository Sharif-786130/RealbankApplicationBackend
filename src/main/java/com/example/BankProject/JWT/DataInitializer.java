package com.example.BankProject.JWT;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.User;
import com.example.BankProject.Repository.UserRepository;
@Component
public class DataInitializer implements CommandLineRunner{
	
	private final UserRepository userRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	public DataInitializer(UserRepository userRepo,PasswordEncoder passwordEncoder) {
		this.userRepo=userRepo;
		this.passwordEncoder=passwordEncoder;
	}
	
	@Override
	public void run(String... args) {
		
		if(userRepo.countByRole(Role.SUPER_ADMIN) ==0) {
			
			User superadmin = new User();
			superadmin.setName("Super Admin");
			superadmin.setEmail("superadmin@bank.com");
			superadmin.setPassword(passwordEncoder.encode("superadmin123"));
			superadmin.setRole(Role.SUPER_ADMIN);
			superadmin.setActive(true);
			superadmin.setPhone("93982457381");
			superadmin.setCreatedAt(LocalDateTime.now());
			superadmin.setPasswordResetRequired(false);
			
			userRepo.save(superadmin);
			
			System.out.println("SuperAdmin created Successfully");
		}
	}
}