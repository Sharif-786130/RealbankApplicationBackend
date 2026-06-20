package com.example.BankProject.Exception;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;





@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	
	private final ObjectMapper objectMapper;
	
	public CustomAccessDeniedHandler(ObjectMapper ObjectMapper) {
		this.objectMapper=ObjectMapper;
	}
	
	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException,ServletException{
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		
		HashMap<String, Object> body =new HashMap<>();
		body.put("status", 403);
		body.put("message", "Access denied. Dmin Previleges required.");
		
		objectMapper.writeValue(response.getOutputStream(), body);
		
	}

}
