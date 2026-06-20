package com.example.BankProject.ServiceImple;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BankProject.CustomerDTO.CreateCustomerResponseDTO;
import com.example.BankProject.CustomerDTO.CustomerDetailsResponseDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerRequestDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerResponseDTO;
import com.example.BankProject.ENUM.KycStatus;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.User;
import com.example.BankProject.MainService.CustomerService;
import com.example.BankProject.MainService.EmailService;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.UserRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public CustomerServiceImpl(
            CustomerRepository customerRepo,
            PasswordEncoder passwordEncoder,
            UserRepository userRepo,
            EmailService emailService
    ) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    // ================= CREATE CUSTOMER =================

    @Override
    public Customer createCustomer(Customer customer, Long officerId, String branchCode) {

        customer.setCreatedByOfficerId(officerId);
        customer.setBranchCode(branchCode);
        customer.setCustomerNumber(generateCustomerNumber());

        // Temp password
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // Create USER
        User user = new User();
        user.setName(customer.getFirstName());
        user.setEmail(customer.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user.setPasswordResetRequired(true);

        User savedUser = userRepo.save(user);

        // Link user with customer
        customer.setUserId(savedUser.getId());
        customer.setActive(true);
        customer.setKycStatus(KycStatus.PENDING);

        Customer savedCustomer = customerRepo.save(customer);

//        try {
//            emailService.sendCustomerWelcomeEmail(
//                    savedCustomer.getEmail(),
//                    tempPassword
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Customer created but email sending failed");
//        }

        return savedCustomer;
    }

    // ================= GET CUSTOMER DETAILS =================

    @Override
    public CustomerDetailsResponseDTO getCustomerById(Long customerId, String branchCode) {

//        Customer customer = customerRepo
//                .findByCustomerIdAndBranchCode(customerId, branchCode)
//                .orElseThrow(() -> new RuntimeException("Customer not Found"));
    	
    	Customer customer = customerRepo
    	        .findByCustomerIdAccessibleToBranch(customerId, branchCode)
    	        .orElseThrow(() -> new RuntimeException("Customer not Found"));

        CustomerDetailsResponseDTO dto = new CustomerDetailsResponseDTO();

        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerNumber(customer.getCustomerNumber());

        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setGender(customer.getGender());
        dto.setDateofBirth(customer.getDateofBirth());

        dto.setEmail(customer.getEmail());
        dto.setMobileNumber(customer.getMobileNumber());

        dto.setAddressLine1(customer.getAddressLine1());
        dto.setAddressLine2(customer.getAddressLine2());
        dto.setCity(customer.getCity());
        dto.setState(customer.getState());
        dto.setPincode(customer.getPincode());
        dto.setCountry(customer.getCountry());

        dto.setAadhaarNumber(customer.getAadhaarNumber());
        dto.setPanNumber(customer.getPanNumber());

        dto.setKycStatus(customer.getKycStatus().name());
        dto.setActive(customer.isActive());

        dto.setBranchCode(customer.getBranchCode());
        dto.setCreatedByOfficerId(customer.getCreatedByOfficerId());

        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }

    // ================= GET CUSTOMERS LIST =================

//    @Override
//    public List<Customer> getCustomersByBranch(String branchCode) {
//        return customerRepo.findByBranchCode(branchCode);
//    }

    
    @Override
    public List<Customer> getCustomersByBranch(String branchCode) {
        return customerRepo.findByBranchCodeOrBranchCodeIsNull(branchCode);
    }
    // ================= UPDATE CUSTOMER =================

    @Override
    public UpdateCustomerResponseDTO updateCustomerBasicDetails(
            Long customerId,
            UpdateCustomerRequestDTO dto,
            String branchCode
    ) {

//        Customer customer = customerRepo
//                .findByCustomerIdAndBranchCode(customerId, branchCode)
//                .orElseThrow(() -> new RuntimeException("Customer not Found"));
    	
    	Customer customer = customerRepo
    	        .findByCustomerIdAccessibleToBranch(customerId, branchCode)
    	        .orElseThrow(() -> new RuntimeException("Customer not Found"));

        // Update fields
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setGender(dto.getGender());
        customer.setDateofBirth(dto.getDateofBirth());

        customer.setEmail(dto.getEmail());
        customer.setMobileNumber(dto.getMobileNumber());

        customer.setAddressLine1(dto.getAddressLine1());
        customer.setAddressLine2(dto.getAddressLine2());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setPincode(dto.getPincode());
        customer.setCountry(dto.getCountry());

        customer.setAadhaarNumber(dto.getAadhaarNumber());
        customer.setPanNumber(dto.getPanNumber());

        Customer saved = customerRepo.save(customer);

        return new UpdateCustomerResponseDTO(
                "Customer updated successfully",
                saved.getCustomerId(),
                saved.getCustomerNumber(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getMobileNumber(),
                saved.getGender(),
                saved.getDateofBirth(),
                saved.getAddressLine1(),
                saved.getAddressLine2(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode(),
                saved.getCountry(),
                saved.getAadhaarNumber(),
                saved.getPanNumber(),
                saved.getKycStatus().name(),
                saved.isActive(),
                saved.getUpdatedAt()
        );
    }

    // ================= DEACTIVATE =================

    @Override
    public void deactivatedCustomer(Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not Found"));

        customer.setActive(false);
        customerRepo.save(customer);
    }

    // ================= SIMPLE RESPONSE =================

    @Override
    public CreateCustomerResponseDTO getCustomerById(Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not Found"));

        CreateCustomerResponseDTO response = new CreateCustomerResponseDTO();

        response.setCustomerId(customer.getCustomerId());
        response.setCustomerNumber(customer.getCustomerNumber());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setMobileNumber(customer.getMobileNumber());
        response.setKycStatus(customer.getKycStatus().name());
        response.setActive(customer.isActive());
        response.setCreatedAt(customer.getCreatedAt());

        return response;
    }

    // ================= RESET PASSWORD =================

    @Override
    public void resetPassword(Long userId, String newPassword) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetRequired(false);

        userRepo.save(user);
    }

    // ================= GENERATE CUSTOMER NUMBER =================

    private String generateCustomerNumber() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}