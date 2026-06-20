package com.example.BankProject.MainService;

import java.util.List;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.TransactionDTO.TransactionResponseDTO;
import com.example.BankProject.TransactionDTO.TransferRequestDTO;


public interface TransactionService {
    String transfer(TransferRequestDTO request, CustomUserDetails userDetails);
    List<TransactionResponseDTO> getByCustomer(Long customerId);
    List<TransactionResponseDTO> getByAccount(Long accountId);
}