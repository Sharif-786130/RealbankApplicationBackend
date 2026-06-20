
package com.example.BankProject.ServiceImple;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BankProject.ENUM.CardStatus;
import com.example.BankProject.ENUM.CardType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Card;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.CardRepository;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Security.CustomUserDetails;
import com.example.BankProject.MainService.CardService;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public CardServiceImpl(
            CardRepository cardRepository,
            AccountRepository accountRepository,
            CustomerRepository customerRepository) {

        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Map<String, Object> issueCard(
            Long accountId,
            Long customerId,
            String cardType,
            CustomUserDetails userDetails) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (cardRepository.existsByAccountAccountId(accountId)) {
            throw new RuntimeException("A card already exists for this account");
        }

        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCvv());
        card.setCardType(CardType.valueOf(cardType.toUpperCase()));
        card.setAccount(account);
        card.setCustomer(customer);
        card.setIssuedByOfficerId(userDetails.getId());

        cardRepository.save(card);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Card issued successfully");
        response.put("cardNumber", maskCard(card.getCardNumber()));
        response.put("cardType", card.getCardType());
        response.put("expiryDate", card.getExpiryDate());
        response.put("status", card.getStatus());

        return response;
    }

    @Override
    public List<Map<String, Object>> getCardsByCustomer(Long customerId) {

        return cardRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(card -> {

                    Map<String, Object> result = new HashMap<>();

                    result.put("cardId", card.getCardId());
                    result.put("cardNumber", maskCard(card.getCardNumber()));
                    result.put("cardType", card.getCardType());
                    result.put("status", card.getStatus());
                    result.put("expiryDate", card.getExpiryDate());
                    result.put("issuedAt", card.getIssuedAt());

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> blockCard(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setStatus(CardStatus.BLOCKED);

        cardRepository.save(card);

        return Map.of("message", "Card blocked successfully");
    }

    @Override
    public Map<String, String> activateCard(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setStatus(CardStatus.ACTIVE);

        cardRepository.save(card);

        return Map.of("message", "Card activated successfully");
    }

    @Override
    public List<Map<String, Object>> getAllCards() {

        return cardRepository.findAll()
                .stream()
                .map(card -> {

                    Map<String, Object> result = new HashMap<>();

                    result.put("cardId", card.getCardId());
                    result.put("cardNumber", maskCard(card.getCardNumber()));
                    result.put("cardType", card.getCardType());
                    result.put("status", card.getStatus());
                    result.put("expiryDate", card.getExpiryDate());
                    result.put("customerId", card.getCustomer().getCustomerId());
                    result.put("customerName",
                            card.getCustomer().getFirstName()
                                    + " "
                                    + card.getCustomer().getLastName());

                    result.put("accountId", card.getAccount().getAccountId());

                    return result;
                })
                .collect(Collectors.toList());
    }

    // Helper Methods

    private String generateCardNumber() {
        Random random = new Random();
        return String.format("4%015d",
                Math.abs(random.nextLong() % 1000000000000000L));
    }

    private String generateCvv() {
        return String.format("%03d",
                new Random().nextInt(1000));
    }

    private String maskCard(String cardNumber) {

        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        return "**** **** **** "
                + cardNumber.substring(cardNumber.length() - 4);
    }
}