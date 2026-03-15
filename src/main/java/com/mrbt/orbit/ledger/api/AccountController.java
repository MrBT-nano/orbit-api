package com.mrbt.orbit.ledger.api;

import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.api.request.CreateAccountRequest;
import com.mrbt.orbit.ledger.api.response.AccountResponse;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.port.in.CreateAccountUseCase;
import com.mrbt.orbit.ledger.core.port.in.GetAccountUseCase;
import com.mrbt.orbit.ledger.infrastructure.mapper.AccountMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Ledger account management for tracking fiat and crypto assets")
public class AccountController {

	private final CreateAccountUseCase createAccountUseCase;
	private final GetAccountUseCase getAccountUseCase;
	private final AccountMapper accountMapper;

	@PostMapping
	@Operation(summary = "Create a new account", description = "Creates a new financial account for a specific user")
	public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
			@Valid @RequestBody CreateAccountRequest request) {
		Account domainAccount = accountMapper.toDomain(request);
		Account createdAccount = createAccountUseCase.createAccount(domainAccount);
		AccountResponse response = accountMapper.toResponse(createdAccount);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Account created successfully", response));
	}

	@GetMapping("/{accountId}")
	@Operation(summary = "Get account by ID", description = "Retrieves a specific account by its ID")
	public ResponseEntity<ApiResponse<AccountResponse>> getAccountById(@PathVariable UUID accountId) {
		Account account = getAccountUseCase.getAccountById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "ID", accountId));

		return ResponseEntity.ok(ApiResponse.success(accountMapper.toResponse(account)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get all accounts for a user", description = "Retrieves all accounts belonging to a specific user")
	public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByUserId(@PathVariable UUID userId) {
		List<Account> accounts = getAccountUseCase.getAccountsByUserId(userId);
		List<AccountResponse> responses = accountMapper.toResponseList(accounts);

		return ResponseEntity.ok(ApiResponse.success(responses));
	}
}
