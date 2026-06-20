package com.example.BankProject.MainService;

import java.util.List;
import java.util.Map;

import com.example.BankProject.Security.CustomUserDetails;

public interface CardService {

    Map<String, Object> issueCard(
            Long accountId,
            Long customerId,
            String cardType,
            CustomUserDetails userDetails);

    List<Map<String, Object>> getCardsByCustomer(Long customerId);

    Map<String, String> blockCard(Long cardId);

    Map<String, String> activateCard(Long cardId);

    List<Map<String, Object>> getAllCards();
}