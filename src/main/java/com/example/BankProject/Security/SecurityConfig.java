package com.example.BankProject.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.BankProject.Exception.CustomAccessDeniedHandler;
import com.example.BankProject.Exception.CustomAuthenticationEntryPoint;
import com.example.BankProject.JWT.JwtFilter;
import com.example.BankProject.Repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final UserRepository userRepo;
	private final CustomAccessDeniedHandler deniedHandler;
	private final CustomAuthenticationEntryPoint authEntryPoint;

	public SecurityConfig(JwtFilter jwtFilter, UserRepository userRepo, CustomAccessDeniedHandler deniedHandler,
			CustomAuthenticationEntryPoint authEntryPoint) {
		this.jwtFilter = jwtFilter;
		this.userRepo = userRepo;
		this.deniedHandler = deniedHandler;
		this.authEntryPoint = authEntryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth

						// Public
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/auth/**", "/error")
						.permitAll().requestMatchers(HttpMethod.POST, "/admin/first-admin").permitAll()

						// Super Admin
						.requestMatchers("/super-admin/**").hasRole("SUPER_ADMIN")

						// Admin (super-admin can also access admin endpoints)
						.requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")

						// Officer only endpoints (super-admin can also view officer data)
						.requestMatchers("/api/officer/**").hasAnyRole("OFFICER", "SUPER_ADMIN")
						.requestMatchers("/employee/**").hasAnyRole("OFFICER", "SUPER_ADMIN")

						// Loans
						.requestMatchers(HttpMethod.GET, "/api/loans/customer/**")
						.hasAnyRole("OFFICER", "ADMIN", "CUSTOMER", "SUPER_ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/loans/**").hasAnyRole("OFFICER", "ADMIN", "SUPER_ADMIN")
//						.requestMatchers(HttpMethod.POST, "/api/loans/**").hasAnyRole("OFFICER")
						.requestMatchers(HttpMethod.POST, "/api/loans/**")
						.hasAnyRole("OFFICER", "CUSTOMER")
						.requestMatchers(HttpMethod.PUT, "/api/loans/**").hasAnyRole("OFFICER", "ADMIN", "SUPER_ADMIN")

						// Accounts
						.requestMatchers(HttpMethod.GET, "/api/account/customer/**")
						.hasAnyRole("OFFICER", "CUSTOMER", "SUPER_ADMIN").requestMatchers("/api/account/**")
						.hasAnyRole("OFFICER", "ADMIN", "SUPER_ADMIN")

						// Cards
						.requestMatchers(HttpMethod.POST, "/api/cards/issue").hasRole("OFFICER")
						.requestMatchers(HttpMethod.GET, "/api/cards/customer/**")
						.hasAnyRole("CUSTOMER", "OFFICER", "ADMIN").requestMatchers(HttpMethod.PATCH, "/api/cards/**")
						.hasAnyRole("ADMIN", "OFFICER").requestMatchers(HttpMethod.GET, "/api/cards/all")
						.hasAnyRole("ADMIN", "SUPER_ADMIN")

						// Tickets
						.requestMatchers(HttpMethod.POST, "/api/tickets/raise").hasRole("CUSTOMER")
						.requestMatchers(HttpMethod.GET, "/api/tickets/customer/**")
						.hasAnyRole("CUSTOMER", "OFFICER", "ADMIN").requestMatchers(HttpMethod.GET, "/api/tickets/open")
						.hasAnyRole("OFFICER", "ADMIN").requestMatchers(HttpMethod.PATCH, "/api/tickets/**")
						.hasAnyRole("OFFICER", "ADMIN").requestMatchers(HttpMethod.GET, "/api/tickets/all")
						.hasAnyRole("ADMIN", "SUPER_ADMIN")

						// Swagger UI — allow public access
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs")
						.permitAll()

						// Transactions
						.requestMatchers(HttpMethod.GET, "/api/transactions/customer/**")
						.hasAnyRole("OFFICER", "CUSTOMER", "SUPER_ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/transactions/account/**")
						.hasAnyRole("OFFICER", "CUSTOMER", "SUPER_ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/transactions/**").hasAnyRole("OFFICER", "CUSTOMER")

						// Customer endpoints
						.requestMatchers("/api/customer/**").hasRole("CUSTOMER")

						.anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint).accessDeniedHandler(deniedHandler))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}