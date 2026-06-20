package com.example.BankProject.ServiceImple;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BankProject.ENUM.LoanStatus;
import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Entity.Loan;
import com.example.BankProject.Entity.User;
import com.example.BankProject.LoanDTO.LoanRequestDTO;
import com.example.BankProject.LoanDTO.LoanResponseDTO;
import com.example.BankProject.MainService.LoanService;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Repository.LoanRepository;
import com.example.BankProject.Security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class LoanServiceImpl implements LoanService {

    // ── Approval limits ────────────────────────────────────────────────────
    private static final BigDecimal OFFICER_LIMIT = BigDecimal.valueOf(100_000);   // ₹1,00,000
    private static final BigDecimal ADMIN_LIMIT   = BigDecimal.valueOf(500_000);   // ₹5,00,000

    // ── Interest rates (single source of truth — matches frontend) ─────────
    private static final double RATE_HOME     = 8.5;
    private static final double RATE_PERSONAL = 12.0;
    private static final double RATE_VEHICLE  = 9.0;   // FIX: was 10.5 in old frontend — now both use 9.0

    private final LoanRepository    loanRepo;
    private final CustomerRepository customerRepo;
    private final AccountRepository  accountRepo;

    public LoanServiceImpl(LoanRepository loanRepo,
                           CustomerRepository customerRepo,
                           AccountRepository accountRepo) {
        this.loanRepo     = loanRepo;
        this.customerRepo = customerRepo;
        this.accountRepo  = accountRepo;
    }

    // ── CREATE ─────────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO request,
                                      CustomUserDetails userDetails) {

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Max 2 active/pending loans per customer
        long activeLoanCount = loanRepo
                .findByCustomerCustomerId(request.getCustomerId())
                .stream()
                .filter(l -> l.getStatus() == LoanStatus.PENDING ||
                             l.getStatus() == LoanStatus.APPROVED)
                .count();

        if (activeLoanCount >= 2) {
            throw new RuntimeException(
                    "Customer already has the maximum number of active loans (2)");
        }

        Loan loan = new Loan();
        loan.setOfficer(userDetails.getUser());
        loan.setStatus(LoanStatus.PENDING);
        loan.setLoanNumber(generateLoanNumber());
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterstRate(getInterestRate(request.getLoanType()));  // uses helper below
        loan.setCustomer(customer);

        return mapToResponse(loanRepo.save(loan));
    }

    // ── GET ALL ────────────────────────────────────────────────────────────
//    @Override
//    public List<LoanResponseDTO> getAllLoans() {
//        return loanRepo.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
    
    @Override
    public Page<LoanResponseDTO> getAllLoans(Pageable pageable) {
        return loanRepo.findAll(pageable)
                .map(loan -> mapToResponse(loan));
    }


    // ── GET BY ID ──────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO getLoanById(Long loanId) {
        return mapToResponse(loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found")));
    }

    // ── GET BY CUSTOMER ────────────────────────────────────────────────────
//    @Override
//    public List<LoanResponseDTO> getLoanByCustomer(Long customerId) {
//        return loanRepo.findByCustomerCustomerId(customerId)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
    @Override
    public Page<LoanResponseDTO> getLoanByCustomer(Long customerId, Pageable pageable) {
        return loanRepo.findByCustomerCustomerId(customerId, pageable)
                .map(loan -> mapToResponse(loan));
    }
    

    // ── APPROVE ────────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO approveLoan(Long loanId,
                                       CustomUserDetails userDetails) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // Guard: only PENDING loans can be approved
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan is already " + loan.getStatus().name());
        }

        User user  = userDetails.getUser();
        Role role  = user.getRole();
        BigDecimal amount = loan.getLoanAmount();

        // ── APPROVAL LIMIT CHECK ───────────────────────────────────────────
        // BUG WAS HERE: role was fetched correctly but the null-safety check
        // was missing — if role is somehow null the limits were skipped entirely.
        if (role == null) {
            throw new RuntimeException("Approver role could not be determined");
        }

        if (role == Role.OFFICER && amount.compareTo(OFFICER_LIMIT) > 0) {
            throw new RuntimeException(
                    "Officers can only approve loans up to ₹1,00,000. " +
                    "This loan (₹" + amount.toPlainString() + ") requires Admin approval.");
        }

        if (role == Role.ADMIN && amount.compareTo(ADMIN_LIMIT) > 0) {
            throw new RuntimeException(
                    "Admins can only approve loans up to ₹5,00,000. " +
                    "This loan (₹" + amount.toPlainString() + ") cannot be approved.");
        }

        // ── SET APPROVAL DETAILS ───────────────────────────────────────────
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedBy(user);
        loan.setApprovedAt(LocalDateTime.now());
        loan.setDisbursedAt(LocalDateTime.now());

        // ── CALCULATE & STORE EMI ──────────────────────────────────────────
        BigDecimal emi = calculateEMI(
                loan.getLoanAmount(),
                loan.getInterstRate(),
                loan.getTenureMonths());
        loan.setEmiAmount(emi);

        // Outstanding balance starts at full principal
        loan.setOutstandingBalance(loan.getLoanAmount());

        // ── CREDIT LOAN AMOUNT TO CUSTOMER'S FIRST ACCOUNT ────────────────
        List<Account> accounts = accountRepo
                .findByCustomerCustomerId(loan.getCustomer().getCustomerId());

        if (accounts.isEmpty()) {
            throw new RuntimeException(
                    "No bank account found for this customer. " +
                    "Please create an account before approving a loan.");
        }

        Account account = accounts.get(0);
        account.setBalance(account.getBalance().add(loan.getLoanAmount()));
        accountRepo.save(account);

        return mapToResponse(loanRepo.save(loan));
    }

    // ── PAY EMI ────────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO payEmi(Long loanId, BigDecimal amount) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Only APPROVED loans can have EMI payments");
        }

        BigDecimal remaining = loan.getOutstandingBalance().subtract(amount);
        loan.setOutstandingBalance(remaining.max(BigDecimal.ZERO));  // never go below 0

        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.CLOSED);
        }

        return mapToResponse(loanRepo.save(loan));
    }

    // ── REJECT ─────────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO rejectLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Only PENDING loans can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);
        return mapToResponse(loanRepo.save(loan));
    }

    // ── CLOSE ──────────────────────────────────────────────────────────────
    @Override
    public LoanResponseDTO closeLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus(LoanStatus.CLOSED);
        return mapToResponse(loanRepo.save(loan));
    }

    // ── PRIVATE HELPERS ────────────────────────────────────────────────────

    private double getInterestRate(String loanType) {
        switch (loanType.toUpperCase()) {
            case "HOME":     return RATE_HOME;
            case "PERSONAL": return RATE_PERSONAL;
            case "VEHICLE":  return RATE_VEHICLE;
            default: throw new RuntimeException(
                    "Invalid loan type: " + loanType +
                    ". Allowed values: HOME, PERSONAL, VEHICLE");
        }
    }

    private BigDecimal calculateEMI(BigDecimal principal,
                                    Double annualRate,
                                    Integer months) {
        double r   = annualRate / 12 / 100;   // monthly rate
        int    n   = months;
        double emi = principal.doubleValue()
                     * r * Math.pow(1 + r, n)
                     / (Math.pow(1 + r, n) - 1);
        return BigDecimal.valueOf(emi).round(new MathContext(10));
    }

    private String generateLoanNumber() {
        return "LOAN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    /**
     * Maps Loan entity → LoanResponseDTO.
     * Now includes interestRate, approvedAt, disbursedAt so the
     * frontend can render EMI breakdown and clearance date correctly.
     */
    private LoanResponseDTO mapToResponse(Loan loan) {
        return new LoanResponseDTO(
                loan.getLoanId(),
                loan.getLoanNumber(),
                loan.getLoanType(),
                loan.getLoanAmount(),
                loan.getEmiAmount(),
                loan.getOutstandingBalance(),
                loan.getStatus().name(),
                loan.getTenureMonths(),
                loan.getInterstRate(),        // ← NEW: interest rate sent to frontend
                loan.getCreatedAt(),
                loan.getApprovedAt(),         // ← NEW: used as startDate for clearance date
                loan.getDisbursedAt()         // ← NEW: disbursement date
        );
    }
}