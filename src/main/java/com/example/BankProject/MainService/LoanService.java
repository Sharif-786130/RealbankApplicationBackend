//package com.example.BankProject.MainService;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import com.example.BankProject.LoanDTO.LoanRequestDTO;
//import com.example.BankProject.LoanDTO.LoanResponseDTO;
//import com.example.BankProject.Security.CustomUserDetails;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface LoanService {
//	
//	LoanResponseDTO createLoan(LoanRequestDTO request,
//							   CustomUserDetails userDetails);
//	
//	LoanResponseDTO getLoanById(Long loanId);
//	
//	List<LoanResponseDTO> getLoanByCustomer(Long customerId);
//	
//	LoanResponseDTO approveLoan(Long loanId,CustomUserDetails userDetails);
//	
//	LoanResponseDTO rejectLoan(Long loanId);
//	
//	LoanResponseDTO payEmi(Long loanId, BigDecimal amount);
//	
//	LoanResponseDTO closeLoan(Long loanId);
//	
//	// Add this one line in interface
//	List<LoanResponseDTO> getAllLoans();
//	
//	Page<LoanResponseDTO> getAllLoans(Pageable pageable);
//	Page<LoanResponseDTO> getLoanByCustomer(Long customerId, Pageable pageable);
//	
//	Page<LoanResponseDTO> findByCustomerCustomerId(Long customerId, Pageable pageable);
//	
//
//}


package com.example.BankProject.MainService;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.BankProject.LoanDTO.LoanRequestDTO;
import com.example.BankProject.LoanDTO.LoanResponseDTO;
import com.example.BankProject.Security.CustomUserDetails;

public interface LoanService {

    LoanResponseDTO createLoan(LoanRequestDTO request, CustomUserDetails userDetails);

    Page<LoanResponseDTO> getAllLoans(Pageable pageable);

    LoanResponseDTO getLoanById(Long loanId);

    Page<LoanResponseDTO> getLoanByCustomer(Long customerId, Pageable pageable);

    LoanResponseDTO approveLoan(Long loanId, CustomUserDetails userDetails);

    LoanResponseDTO rejectLoan(Long loanId);

    LoanResponseDTO payEmi(Long loanId, BigDecimal amount);

    LoanResponseDTO closeLoan(Long loanId);
}