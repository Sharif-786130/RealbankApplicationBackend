package com.example.BankProject.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.CustomerDTO.RegisterCustomerRequestDTO;
import com.example.BankProject.DTO.LoginRequestDTO;
import com.example.BankProject.DTO.LoginResponseDTO;
import com.example.BankProject.DTO.VerifyOtpRequestDTO;
import com.example.BankProject.Entity.User;
import com.example.BankProject.JWT.JwtUtil;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.ServiceImple.CustomerRegistrationService;
import com.example.BankProject.ServiceImple.UserServiceImple;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "1. Authentication", description = "Login, logout, token refresh and current user")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserServiceImple userService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepo;
    private final CustomerRegistrationService registrationService;

    public AuthController(JwtUtil jwtUtil,
                          UserServiceImple userService,
                          AuthenticationManager authenticationManager,
                          CustomerRepository customerRepo,
                          CustomerRegistrationService registrationService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.customerRepo = customerRepo;
        this.registrationService = registrationService;
    }

    @Operation(summary = "Login", description = "Authenticate with email and password. Returns JWT access token and sets refresh token cookie.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody LoginRequestDTO dto,
            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(), dto.getPassword()));

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userService.getByEmail(userDetails.getUsername());

        String role = userDetails.getAuthorities().iterator()
                .next().getAuthority().replace("ROLE_", "");

        String accessToken = jwtUtil.generateAccessToken(
                userDetails.getUsername(), role);
        String refreshToken = jwtUtil.generateRefreshToken(
                userDetails.getUsername());

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true).secure(false).path("/")
                .maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Long customerId = null;
        if (role.equals("CUSTOMER")) {
            customerId = customerRepo
                    .findByEmail(userDetails.getUsername())
                    .map(c -> c.getCustomerId())
                    .orElse(null);
        }

        return new LoginResponseDTO(
                accessToken, null, role,
                user.getPasswordResetRequired(), customerId);
    }

    @Operation(summary = "Register", description = "Customer self-registration. Creates an unverified account and sends an OTP to the given email.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registered, OTP sent"),
        @ApiResponse(responseCode = "400", description = "Validation error or email already exists")
    })
    @PostMapping("/register")
    public Map<String, String> register(@jakarta.validation.Valid @RequestBody RegisterCustomerRequestDTO dto) {
        registrationService.register(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful. Please check your email for the OTP to verify your account.");
        return response;
    }

    @Operation(summary = "Verify OTP", description = "Verifies the OTP sent at registration and activates the account. Returns JWT access token (auto-login) and sets refresh token cookie.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OTP verified, account activated"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    @PostMapping("/verify-otp")
    public LoginResponseDTO verifyOtp(
            @RequestBody VerifyOtpRequestDTO dto,
            HttpServletResponse response) {
        return registrationService.verifyOtp(dto.getEmail(), dto.getOtp(), response);
    }

    @Operation(summary = "Resend OTP", description = "Resends a new OTP to the given email if the account is not yet verified.")
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestBody VerifyOtpRequestDTO dto) {
        registrationService.resendOtp(dto.getEmail());
        return "A new OTP has been sent to your email.";
    }

    @Operation(summary = "Refresh token", description = "Use the HttpOnly refresh token cookie to get a new access token.")
    @ApiResponse(responseCode = "200", description = "New access token returned")
    @PostMapping("/refresh")
    public LoginResponseDTO refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userService.getByEmail(email);
        String role = user.getRole().name();

        String newAccessToken = jwtUtil.generateAccessToken(email, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        ResponseCookie newCookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true).secure(false).path("/")
                .maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();
        response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

        Long customerId = null;
        if (role.equals("CUSTOMER")) {
            customerId = customerRepo.findByEmail(email)
                    .map(c -> c.getCustomerId())
                    .orElse(null);
        }

        return new LoginResponseDTO(
                newAccessToken, null, role,
                user.getPasswordResetRequired(), customerId);
    }

    @Operation(summary = "Logout", description = "Clears the refresh token cookie.")
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true).secure(false).path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return "Logged out successfully";
    }

    @Operation(summary = "Current user", description = "Returns the logged-in user's role and details.", security = @SecurityRequirement(name = "Bearer Auth"))
    @GetMapping("/me")
    public LoginResponseDTO me(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.getByEmail(userDetails.getUsername());
        String role = user.getRole().name();

        Long customerId = null;
        if (role.equals("CUSTOMER")) {
            customerId = customerRepo
                    .findByEmail(userDetails.getUsername())
                    .map(c -> c.getCustomerId())
                    .orElse(null);
        }

        return new LoginResponseDTO(
                null, null, role,
                user.getPasswordResetRequired(), customerId);
    }
}