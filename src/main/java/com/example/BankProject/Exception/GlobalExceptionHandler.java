 package com.example.BankProject.Exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex){
		
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		
		return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, String>> handleAccessDenied(){
		
		Map<String, String> error = new HashMap<>();
		error.put("error","Access denied");
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

}
