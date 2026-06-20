package com.example.BankProject.ServiceImple;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BankProject.DTO.CreateUserDTO;
import com.example.BankProject.Entity.User;
import com.example.BankProject.MainService.UserService;
import com.example.BankProject.Mapper.UserMapper;
import com.example.BankProject.Repository.UserRepository;

@Service
public class UserServiceImple implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImple(UserRepository userRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(CreateUserDTO dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }

    @Override
    public User authenticate(String email, String rawPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not Found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
