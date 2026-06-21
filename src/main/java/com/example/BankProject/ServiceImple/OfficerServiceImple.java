package com.example.BankProject.ServiceImple;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.User;
import com.example.BankProject.MainService.OfficerService;
import com.example.BankProject.OfficerDTO.CreateOfficerRequestDTO;
import com.example.BankProject.OfficerDTO.UpdateOfficerRequestDTO;
import com.example.BankProject.Repository.UserRepository;
@Service
public class OfficerServiceImple implements OfficerService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public OfficerServiceImple(UserRepository userRepo,
                              PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User createOfficer(CreateOfficerRequestDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User officer = new User();
        officer.setName(dto.getName());
        officer.setEmail(dto.getEmail());
        officer.setPhone(dto.getPhone());
        officer.setRole(Role.OFFICER);
        officer.setActive(true);
        officer.setEmployeeId(System.currentTimeMillis());
        officer.setPassword(passwordEncoder.encode(dto.getPassword()));
        officer.setPasswordResetRequired(true);
        return userRepo.save(officer);
    }
    @Override
    public List<User> getAllOfficers() {
        return userRepo.findByRole(Role.OFFICER);
    }
    @Override
    public User getOfficerById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Officer not Found"));
        if (user.getRole() != Role.OFFICER) {
            throw new RuntimeException("User is not an officer");
        }
        return user;
    }
    @Override
    public List<User> getOfficerByName(String name) {
        return userRepo.findByNameContainingIgnoreCaseAndRole(name, Role.OFFICER);
    }
	@Override
	public User updateOfficer(Long id, UpdateOfficerRequestDTO dto) {
		User officer = getOfficerById(id);
		// name
		if (dto.getName() != null && !dto.getName().isBlank()) {
			officer.setName(dto.getName());
		}
		// phone
		if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
			officer.setPhone(dto.getPhone());
		}
		if (dto.getActive() != null) {
			officer.setActive(dto.getActive());
		}
		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			if (!dto.getEmail().equals(officer.getEmail()) && userRepo.existsByEmail(dto.getEmail())) {
				throw new RuntimeException("Email already Exists");
			}
			officer.setEmail(dto.getEmail());
		}
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			officer.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		return userRepo.save(officer);
	}
    @Override
    public void deleteOfficer(Long id) {
        User officer = userRepo.findById(id)
        		.orElseThrow(() -> 
        				new RuntimeException("Officer not found with id: " + id));
        
        userRepo.delete(officer);
    }
}