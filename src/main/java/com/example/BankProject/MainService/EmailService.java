package com.example.BankProject.MainService;

public interface EmailService {
//	void sendCustomerWelcomeEmail(String Email,String tempPassword);
	
	   void sendTransactionEmail(
	            String email,
	            String transactionType,
	            Double amount,
	            String accountNumber,
	            Double availableBalance
	    );
	   
		void sendOtpEmail(String email, String otp);

}
