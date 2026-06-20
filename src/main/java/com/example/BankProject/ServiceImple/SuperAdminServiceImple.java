package com.example.BankProject.ServiceImple;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BankProject.AdminDTO.CreateAdminRequestDTO;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.User;
import com.example.BankProject.MainService.SuperAdminService;
import com.example.BankProject.Repository.UserRepository;

@Service
public class SuperAdminServiceImple implements SuperAdminService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_ADMINS = 4;

    public SuperAdminServiceImple(UserRepository userRepo,
                                 PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createAdmin(CreateAdminRequestDTO dto) {

        long adminCount = userRepo.countByRole(Role.ADMIN);
        if (adminCount >= MAX_ADMINS) {
            throw new RuntimeException(
                "Admin limit reached. Max allowed " + MAX_ADMINS
            );
        }

        User admin = new User();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setPhone(dto.getPhone());
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        return userRepo.save(admin);
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepo.findByRole(Role.ADMIN);
    }

    @Override
    public User getAdminById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not Found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an Admin");
        }
        return user;
    }

    @Override
    public void deleteAdmin(Long id) {

        long adminCount = userRepo.countByRole(Role.ADMIN);
        if (adminCount <= 1) {
            throw new RuntimeException("Cannot delete the last Admin");
        }

        User admin = getAdminById(id);
        userRepo.delete(admin);
    }
}