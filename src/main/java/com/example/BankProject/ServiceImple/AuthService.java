//package com.example.BankProject.Service;
//
//import org.springframework.stereotype.Service;
//
//import com.example.BankProject.Entity.User;
//
//@Service
//public class AuthService {
//	
//	private final UserService userService;
//	
//	public AuthService(UserService userService) {
//		this.userService=userService;
//	}
//	
//	public User authenticate(String email,String password) {
//		
//		User user =userService.findByEmail(email)
//				.orElseThrow(() -> new RuntimeException("User not Found"));
//		
//		if(!password.equals(user.getPassword())) {
//			throw new RuntimeException("Invalid Password");
//		}
//		return user;
//		
//	}
//	
//
//
//
//}
//package com;





