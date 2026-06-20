package com.example.BankProject.Exception;




import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
private final ObjectMapper objectMapper;
	
	public CustomAuthenticationEntryPoint(ObjectMapper ObjectMapper) {
		this.objectMapper=ObjectMapper;
	}
	
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException)
			throws IOException{
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
			
		HashMap<String, Object> body =new HashMap<>();
		body.put("status", 401);
		body.put("message", "Authentication required. Please login");
		body.put("path", request.getRequestURI());
		
		objectMapper.writeValue(response.getOutputStream(), body);

	}
}
