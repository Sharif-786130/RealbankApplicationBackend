package com.example.BankProject.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.BankProject.ENUM.CardStatus;
import com.example.BankProject.ENUM.CardType;
import com.example.BankProject.Entity.Account;
import com.example.BankProject.Entity.Card;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.Repository.AccountRepository;
import com.example.BankProject.Repository.CardRepository;
import com.example.BankProject.Repository.CustomerRepository;
import com.example.BankProject.Security.CustomUserDetails;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:5173")
public class CardController {

	private final CardRepository cardRepo;
	private final AccountRepository accountRepo;
	private final CustomerRepository customerRepo;

	public CardController(CardRepository cardRepo, AccountRepository accountRepo, CustomerRepository customerRepo) {
		this.cardRepo = cardRepo;
		this.accountRepo = accountRepo;
		this.customerRepo = customerRepo;
	}

	// ── Officer: Issue a card to a customer account ───────────────────────────
	@PostMapping("/issue")
	@PreAuthorize("hasRole('OFFICER')")
	public ResponseEntity<?> issueCard(@RequestBody Map<String, Object> request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long accountId = Long.parseLong(request.get("accountId").toString());
		Long customerId = Long.parseLong(request.get("customerId").toString());
		String cardType = request.get("cardType").toString();

		Account account = accountRepo.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

		Customer customer = customerRepo.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		if (cardRepo.existsByAccountAccountId(accountId)) {
			return ResponseEntity.badRequest().body(Map.of("message", "A card already exists for this account"));
		}

		Card card = new Card();
		card.setCardNumber(generateCardNumber());
		card.setCvv(generateCvv());
		card.setCardType(CardType.valueOf(cardType));
		card.setAccount(account);
		card.setCustomer(customer);
		card.setIssuedByOfficerId(userDetails.getId());

		cardRepo.save(card);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Card issued successfully");
		response.put("cardNumber", maskCard(card.getCardNumber()));
		response.put("cardType", card.getCardType());
		response.put("expiryDate", card.getExpiryDate().toString());
		response.put("status", card.getStatus());
		return ResponseEntity.ok(response);
	}

	// ── Customer: Get my cards ────────────────────────────────────────────────
	@GetMapping("/customer/{customerId}")
	@PreAuthorize("hasAnyRole('CUSTOMER','OFFICER','ADMIN')")
	public ResponseEntity<?> getCardsByCustomer(@PathVariable Long customerId) {
		List<Card> cards = cardRepo.findByCustomerCustomerId(customerId);
		List<Map<String, Object>> result = cards.stream().map(c -> {
			Map<String, Object> m = new HashMap<>();
			m.put("cardId", c.getCardId());
			m.put("cardNumber", maskCard(c.getCardNumber()));
			m.put("cardType", c.getCardType());
			m.put("status", c.getStatus());
			m.put("expiryDate", c.getExpiryDate());
			m.put("issuedAt", c.getIssuedAt());
			return m;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}

	// ── Admin / Officer: Block a card ─────────────────────────────────────────
	@PatchMapping("/{cardId}/block")
	@PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
	public ResponseEntity<?> blockCard(@PathVariable Long cardId) {
		Card card = cardRepo.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
		card.setStatus(CardStatus.BLOCKED);
		cardRepo.save(card);
		return ResponseEntity.ok(Map.of("message", "Card blocked successfully"));
	}

	// ── Admin / Officer: Activate a card ──────────────────────────────────────
	@PatchMapping("/{cardId}/activate")
	@PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
	public ResponseEntity<?> activateCard(@PathVariable Long cardId) {
		Card card = cardRepo.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
		card.setStatus(CardStatus.ACTIVE);
		cardRepo.save(card);
		return ResponseEntity.ok(Map.of("message", "Card activated successfully"));
	}

	// ── Admin: Get all cards ──────────────────────────────────────────────────
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<?> getAllCards() {
		List<Card> cards = cardRepo.findAll();
		List<Map<String, Object>> result = cards.stream().map(c -> {
			Map<String, Object> m = new HashMap<>();
			m.put("cardId", c.getCardId());
			m.put("cardNumber", maskCard(c.getCardNumber()));
			m.put("cardType", c.getCardType());
			m.put("status", c.getStatus());
			m.put("expiryDate", c.getExpiryDate());
			m.put("customerId", c.getCustomer().getCustomerId());
			m.put("customerName", c.getCustomer().getFirstName() + " " + c.getCustomer().getLastName());
			m.put("accountId", c.getAccount().getAccountId());
			return m;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}

	// ── Helpers ───────────────────────────────────────────────────────────────
	private String generateCardNumber() {
		Random r = new Random();
		return String.format("4%015d", Math.abs(r.nextLong() % 1000000000000000L));
	}

	private String generateCvv() {
		return String.format("%03d", new Random().nextInt(1000));
	}

	private String maskCard(String number) {
		if (number == null || number.length() < 4)
			return "****";
		return "**** **** **** " + number.substring(number.length() - 4);
	}
}