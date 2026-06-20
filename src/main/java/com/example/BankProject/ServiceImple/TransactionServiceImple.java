//package com.example.BankProject.ServiceImple;
////
//import org.springframework.stereotype.Service;
//
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.example.BankProject.ENUM.TransactionType;
//import com.example.BankProject.Entity.Account;
//import com.example.BankProject.Entity.Transaction;
//import com.example.BankProject.MainService.TransactionService;
//import com.example.BankProject.Repository.AccountRepository;
//import com.example.BankProject.Repository.TransactionRepository;
//import com.example.BankProject.Security.CustomUserDetails;
//import com.example.BankProject.TransactionDTO.TransactionResponseDTO;
//import com.example.BankProject.TransactionDTO.TransferRequestDTO;
//
//
//@Service
//public class TransactionServiceImple implements TransactionService {
//
//    private final TransactionRepository transactionRepo;
//    private final AccountRepository accountRepo;
//    private final EmailServiceImpl emailServiceimpl;
//
//    public TransactionServiceImple(TransactionRepository transactionRepo,
//            AccountRepository accountRepo,
//            EmailServiceImpl emailServiceimpl) {
//        this.transactionRepo = transactionRepo;
//        this.accountRepo = accountRepo;
//        this.emailServiceimpl = emailServiceimpl;
//    }
//
//    @Override
//    public String transfer(TransferRequestDTO request, CustomUserDetails userDetails) {
//
//        Account fromAccount = accountRepo.findById(request.getFromAccountId())
//                .orElseThrow(() -> new RuntimeException("Sender account not found"));
//
//        Account toAccount = accountRepo.findByAccountNumber(request.getToAccountNumber())
//                .orElseThrow(() -> new RuntimeException("Receiver account not found"));
//
//        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
//            throw new RuntimeException("Cannot transfer to same account");
//        }
//
//        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
//            throw new RuntimeException("Insufficient balance");
//        }
//
//        BigDecimal amount = request.getAmount();
//        TransactionType type = TransactionType.valueOf(request.getType());
//
//        // update balances
//        BigDecimal senderBefore = fromAccount.getBalance();
//        fromAccount.setBalance(senderBefore.subtract(amount));
//        accountRepo.save(fromAccount);
//
//        BigDecimal receiverBefore = toAccount.getBalance();
//        toAccount.setBalance(receiverBefore.add(amount));
//        accountRepo.save(toAccount);
//
//        // debit transaction for sender
//        Transaction debit = new Transaction();
//        debit.setAccount(fromAccount);
//        debit.setToAccount(toAccount);
//        debit.setAmount(amount);
//        debit.setType(TransactionType.TRANSFER_DEBIT);
//        debit.setBalanceBefore(senderBefore);
//        debit.setBalanceAfter(fromAccount.getBalance());
//        debit.setDescription(type.name() + " transfer to " + toAccount.getAccountNumber());
//        debit.setUpiId(request.getUpiId());
//        debit.setIfscCode(request.getIfscCode());
//        debit.setPerformedById(userDetails.getUser().getId());
//        transactionRepo.save(debit);
//
//        // credit transaction for receiver
//        Transaction credit = new Transaction();
//        credit.setAccount(toAccount);
//        credit.setToAccount(fromAccount);
//        credit.setAmount(amount);
//        credit.setType(TransactionType.TRANSFER_CREDIT);
//        credit.setBalanceBefore(receiverBefore);
//        credit.setBalanceAfter(toAccount.getBalance());
//        credit.setDescription(type.name() + " received from " + fromAccount.getAccountNumber());
//        credit.setPerformedById(userDetails.getUser().getId());
//        transactionRepo.save(credit);
//        
////        sending the message after the transactions
//        String email = fromAccount
//                .getCustomer()
//                .getEmail();
//
//        emailServiceimpl.sendTransactionEmail(
//                email,
//                "TRANSFER",
//                amount.doubleValue(),
//                fromAccount.getAccountNumber(),
//                fromAccount.getBalance().doubleValue()
//        );
//
//        return "Transfer successful";
//    }
//
//    @Override
//    public List<TransactionResponseDTO> getByCustomer(Long customerId) {
//        return transactionRepo.findByAccountCustomerCustomerId(customerId)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<TransactionResponseDTO> getByAccount(Long accountId) {
//        return transactionRepo.findByAccountAccountIdOrderByCreatedAtDesc(accountId)
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    private TransactionResponseDTO mapToResponse(Transaction t) {
//        return new TransactionResponseDTO(
//                t.getTransactionId(),
//                t.getTransactionNumber(),
//                t.getAmount(),
//                t.getType().name(),
//                t.getBalanceBefore(),
//                t.getBalanceAfter(),
//                t.getDescription(),
//                t.getStatus(),
//                t.getCreatedAt()
//        );
//    }
//}

package com.example.BankProject.ServiceImple;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.example.BankProject.ENUM.TransactionType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Transaction;
import com.example.BankProject.MainService.TransactionService;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.TransactionRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.TransactionDTO.TransactionResponseDTO;
import com.example.BankProject.TransactionDTO.TransferRequestDTO;


@Service
public class TransactionServiceImple implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final EmailServiceImpl emailServiceimpl;

    public TransactionServiceImple(TransactionRepository transactionRepo,
            AccountRepository accountRepo,
            EmailServiceImpl emailServiceimpl) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.emailServiceimpl = emailServiceimpl;
    }

    @Override
    public String transfer(TransferRequestDTO request, CustomUserDetails userDetails) {

        Account fromAccount = accountRepo.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepo.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal amount = request.getAmount();
        TransactionType type = TransactionType.valueOf(request.getType());

        // update balances
        BigDecimal senderBefore = fromAccount.getBalance();
        fromAccount.setBalance(senderBefore.subtract(amount));
        accountRepo.save(fromAccount);

        BigDecimal receiverBefore = toAccount.getBalance();
        toAccount.setBalance(receiverBefore.add(amount));
        accountRepo.save(toAccount);

        // debit transaction for sender
        Transaction debit = new Transaction();
        debit.setAccount(fromAccount);
        debit.setToAccount(toAccount);
        debit.setAmount(amount);
        debit.setType(TransactionType.TRANSFER_DEBIT);
        debit.setBalanceBefore(senderBefore);
        debit.setBalanceAfter(fromAccount.getBalance());
        debit.setDescription(type.name() + " transfer to " + toAccount.getAccountNumber());
        debit.setUpiId(request.getUpiId());
        debit.setIfscCode(request.getIfscCode());
        debit.setPerformedById(userDetails.getUser().getId());
        transactionRepo.save(debit);

        // credit transaction for receiver
        Transaction credit = new Transaction();
        credit.setAccount(toAccount);
        credit.setToAccount(fromAccount);
        credit.setAmount(amount);
        credit.setType(TransactionType.TRANSFER_CREDIT);
        credit.setBalanceBefore(receiverBefore);
        credit.setBalanceAfter(toAccount.getBalance());
        credit.setDescription(type.name() + " received from " + fromAccount.getAccountNumber());
        credit.setPerformedById(userDetails.getUser().getId());
        transactionRepo.save(credit);

        // ── send email to SENDER ──
        String senderEmail = fromAccount.getCustomer().getEmail();
        emailServiceimpl.sendTransactionEmail(
                senderEmail,
                "TRANSFER_DEBIT",
                amount.doubleValue(),
                fromAccount.getAccountNumber(),
                fromAccount.getBalance().doubleValue()
        );

        // ── send email to RECEIVER ──
        String receiverEmail = toAccount.getCustomer().getEmail();
        emailServiceimpl.sendTransactionEmail(
                receiverEmail,
                "TRANSFER_CREDIT",
                amount.doubleValue(),
                toAccount.getAccountNumber(),
                toAccount.getBalance().doubleValue()
        );

        return "Transfer successful";
    }

    @Override
    public List<TransactionResponseDTO> getByCustomer(Long customerId) {
        return transactionRepo.findByAccountCustomerCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDTO> getByAccount(Long accountId) {
        return transactionRepo.findByAccountAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO mapToResponse(Transaction t) {
        return new TransactionResponseDTO(
                t.getTransactionId(),
                t.getTransactionNumber(),
                t.getAmount(),
                t.getType().name(),
                t.getBalanceBefore(),
                t.getBalanceAfter(),
                t.getDescription(),
                t.getStatus(),
                t.getCreatedAt()
        );
    }
}
