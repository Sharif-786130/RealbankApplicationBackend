package com.example.BankProject.ServiceImple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.BankProject.MainService.EmailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    private void sendEmail(String toEmail, String subject, String textContent) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");

        Map<String, Object> sender = new HashMap<>();
        sender.put("name", "Bank Team");
        sender.put("email", senderEmail);

        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", toEmail);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", List.of(recipient));
        body.put("subject", subject);
        body.put("textContent", textContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }

    @Override
    public void sendOtpEmail(String email, String otp) {
        String subject = "Verify your account - OTP";
        String text =
                "Dear Customer,\n\n" +
                "Your One-Time Password (OTP) for account verification is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n" +
                "Thank you,\nBank Team";

        sendEmail(email, subject, text);
    }

    @Override
    public void sendTransactionEmail(
            String email,
            String transactionType,
            Double amount,
            String accountNumber,
            Double availableBalance) {

        String subject = "Transaction Alert";
        String text =
                "Dear Customer,\n\n" +
                "Your transaction has been completed successfully.\n\n" +
                "Transaction Type : " + transactionType + "\n" +
                "Amount           : ₹" + amount + "\n" +
                "Account Number   : " + accountNumber + "\n" +
                "Available Balance: ₹" + availableBalance + "\n\n" +
                "Thank you for banking with us.\n\n" +
                "Bank Team";

        sendEmail(email, subject, text);
    }
}