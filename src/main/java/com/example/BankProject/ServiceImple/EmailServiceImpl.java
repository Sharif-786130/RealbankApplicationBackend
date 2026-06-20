package com.example.BankProject.ServiceImple;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.BankProject.MainService.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    public void sendOtpEmail(String email, String otp) {
    	SimpleMailMessage message = new SimpleMailMessage();
    	message.setTo(email);
    	message.setSubject("Verify your account - OTP");
    	message.setText(
    			"Dear Customer,\n\n" +
    			"Your One-Time Password (OTP) for account verification is: " + otp + "\n\n" +
    			"This OTP is valid for 10 minutes. Do not share it with anyone.\n\n" +
    			"Thank you,\nBank Team"
    		);
    	mailSender.send(message);
    }

//    @Override
//    public void sendCustomerWelcomeEmail(String email, String tempPassword) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setTo(email);
//        message.setSubject("Welcome to Our Bank - Account Created");
//
//        message.setText(
//                "Dear Customer,\n\n" +
//                "Your account has been successfully created.\n\n" +
//                "Temporary Password: " + tempPassword + "\n\n" +
//                "Please login and reset your password immediately.\n\n" +
//                "Thank you,\nBank Team"
//        );
//
//        mailSender.send(message);
//    }

    @Override
    public void sendTransactionEmail(
            String email,
            String transactionType,
            Double amount,
            String accountNumber,
            Double availableBalance) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Transaction Alert");

        message.setText(
                "Dear Customer,\n\n" +
                "Your transaction has been completed successfully.\n\n" +
                "Transaction Type : " + transactionType + "\n" +
                "Amount           : ₹" + amount + "\n" +
                "Account Number   : " + accountNumber + "\n" +
                "Available Balance: ₹" + availableBalance + "\n\n" +
                "Thank you for banking with us.\n\n" +
                "Bank Team"
        );

        mailSender.send(message);
    }
    
    
}