package com.example.BankProject.ServiceImple;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.BankProject.CustomerDTO.RegisterCustomerRequestDTO;
import com.example.BankProject.DTO.LoginResponseDTO;
import com.example.BankProject.ENUM.KycStatus;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.User;
import com.example.BankProject.JWT.JwtUtil;
import com.example.BankProject.MainService.EmailService;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.security.SecureRandom;

@Service
public class CustomerRegistrationService {

	private final UserRepository userRepo;
	private final CustomerRepository customerRepo;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final JwtUtil jwtUtil;

	private static final SecureRandom RANDOM = new SecureRandom();

	public CustomerRegistrationService(
			UserRepository userRepo,
			CustomerRepository customerRepo,
			PasswordEncoder passwordEncoder,
			EmailService emailService,
			JwtUtil jwtUtil) {
		this.userRepo = userRepo;
		this.customerRepo = customerRepo;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.jwtUtil = jwtUtil;
	}

	// ================= SELF REGISTRATION =================
	@Transactional
	public void register(RegisterCustomerRequestDTO dto) {

		if (userRepo.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("An account with this email already exists");
		}

		if (customerRepo.findByEmail(dto.getEmail()).isPresent()) {
			throw new RuntimeException("An account with this email already exists");
		}
		if (customerRepo.findByMobileNumber(dto.getMobileNumber()).isPresent()) {
		    throw new RuntimeException("An account with this mobile number already exists");
		}

		String otp = generateOtp();
		String tempPassword = UUID.randomUUID().toString().substring(0, 8);

		// Create USER — inactive until OTP verified
		User user = new User();
		user.setName(dto.getFirstName() + " " + dto.getLastName());
		user.setEmail(dto.getEmail());
//		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setPassword(passwordEncoder.encode(tempPassword));
		user.setRole(Role.CUSTOMER);
		user.setActive(false); // gated until OTP verified
		user.setPasswordResetRequired(false); // they set their own password
		user.setOtpCode(otp);
		user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

		User savedUser = userRepo.save(user);

		// Create CUSTOMER (business record) — KYC pending until officer review
		Customer customer = new Customer();
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
		customer.setUserId(savedUser.getId());
		customer.setActive(false); // becomes active once OTP verified
		customer.setKycStatus(KycStatus.PENDING); // officer verifies Aadhaar/PAN later
		customer.setCustomerNumber(generateCustomerNumber());
		// createdByOfficerId stays null — self-registered, no officer involved

		customerRepo.save(customer);

		try {
			emailService.sendOtpEmail(dto.getEmail(), otp);
		} catch (Exception e) {
			throw new RuntimeException("Account created but failed to send OTP email");
		}
	}

	// ================= VERIFY OTP + AUTO LOGIN =================

	public LoginResponseDTO verifyOtp(String email, String otp, HttpServletResponse response) {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("No account found for this email"));

		if (user.isActive()) {
			throw new RuntimeException("Account already verified. Please login.");
		}

		if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
			throw new RuntimeException("Invalid OTP");
		}

		if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("OTP has expired. Please request a new one.");
		}

		// Activate user
		user.setActive(true);
		user.setOtpCode(null);
		user.setOtpExpiry(null);
		userRepo.save(user);

		// Activate matching customer record
		Customer customer = customerRepo.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Customer record not found"));
		customer.setActive(true);
		customerRepo.save(customer);

		// Issue tokens — auto login
		String accessToken = jwtUtil.generateAccessToken(user.getEmail(), "CUSTOMER");
		String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", refreshToken)
				.httpOnly(true).secure(false).path("/")
				.maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new LoginResponseDTO(
				accessToken, null, "CUSTOMER",
				true, customer.getCustomerId());
	}

	// ================= RESEND OTP =================

	public void resendOtp(String email) {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("No account found for this email"));

		if (user.isActive()) {
			throw new RuntimeException("Account already verified. Please login.");
		}

		String otp = generateOtp();
		user.setOtpCode(otp);
		user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
		userRepo.save(user);

		try {
			emailService.sendOtpEmail(email, otp);
		} catch (Exception e) {
			throw new RuntimeException("Failed to resend OTP email");
		}
	}

	// ================= HELPERS =================

	private String generateOtp() {
		int otp = 100000 + RANDOM.nextInt(900000); // 6-digit
		return String.valueOf(otp);
	}

	private String generateCustomerNumber() {
		return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
}