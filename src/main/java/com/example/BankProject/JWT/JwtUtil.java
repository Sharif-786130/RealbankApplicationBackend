//package com.example.BankProject.JWT;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//import io.jsonwebtoken.security.Keys;
//import lombok.Value;
//
//
//@Component
//public class JwtUtil {
//	
////	private final String SECRET_KEY = 
////			"my-secret-key-for-bank-project-which-is-very-secure-123456";
//	
//	@Value("${jwt.secret}")
//	private String SECRET_KEY;
//	
//	private final Key key = Keys.hmacShaKeyFor(
//			SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//	
//	
//	public String generateAccessToken(String email, String role) {
//		return Jwts.builder()
//				.setSubject(email)
//				.claim("role", role)
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + 15 *60 * 1000))
//				.signWith(key)
//				.compact();
//	}
//	
//	public String generateRefreshToken(String email) {
//		return Jwts.builder()
//				.setSubject(email)
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
//				.signWith(key)
//				.compact();
//	}
//	
//	public String extractEmail(String token) {
//		return getAllClaims(token).getSubject();
//	}
//	
//	public boolean validateToken(String token) {
//		try {
//			getAllClaims(token);
//			return true;
//		}catch(Exception e) {
//			return false;
//		}
//		
//	}
//	
//	
//	//Internal Method
//	private Claims getAllClaims(String token) {
//		return Jwts.parserBuilder()
//				.setSigningKey(key)
//				.build()
//				.parseClaimsJws(token)
//				.getBody();
//	}
//
//
//}



package com.example.BankProject.JWT;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value; // ← import this
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // ❌ Old way — hardcoded
    // private final String SECRET_KEY = 
    //     "my-secret-key-for-bank-project-which-is-very-secure-123456";

    // ✅ New way — read from properties file
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key key;

    // ✅ Add this method to build key after value is injected
    @jakarta.annotation.PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // rest of your code stays same
    public String generateAccessToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                    System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                    System.currentTimeMillis() + 
                    7L * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}