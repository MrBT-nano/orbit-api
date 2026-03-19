package com.mrbt.orbit.ledger.api;

import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.api.PaginationParams;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.api.request.CreateTransactionRequest;
import com.mrbt.orbit.ledger.api.response.TransactionResponse;
import com.mrbt.orbit.ledger.core.model.Transaction;
import com.mrbt.orbit.ledger.core.port.in.CreateTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.GetTransactionUseCase;
import com.mrbt.orbit.ledger.infrastructure.mapper.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Core ledger entries mapping money movement into and out of accounts")
public class TransactionController {

	private final CreateTransactionUseCase createTransactionUseCase;
	private final GetTransactionUseCase getTransactionUseCase;
	private final TransactionMapper transactionMapper;

	@PostMapping
	@Operation(summary = "Create a transaction", description = "Records a new financial transaction. Automatically updates the associated account's balance.")
	public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
			@Valid @RequestBody CreateTransactionRequest request) {
		Transaction domainTransaction = transactionMapper.toDomain(request);
		Transaction createdTransaction = createTransactionUseCase.createTransaction(domainTransaction);
		TransactionResponse response = transactionMapper.toResponse(createdTransaction);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Transaction recorded successfully", response));
	}

	@GetMapping("/{transactionId}")
	@Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction record")
	public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable UUID transactionId) {
		Transaction transaction = getTransactionUseCase.getTransactionById(transactionId)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction", "ID", transactionId));

		return ResponseEntity.ok(ApiResponse.success(transactionMapper.toResponse(transaction)));
	}

	@GetMapping("/account/{accountId}")
	@Operation(summary = "Get transactions for an account", description = "Retrieves the paginated ledger history for a specific account, newest first")
	public ResponseEntity<ApiResponse<PageResult<TransactionResponse>>> getTransactionsByAccountId(
			@PathVariable UUID accountId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<Transaction> result = getTransactionUseCase.getTransactionsByAccountId(accountId, pagination.page(),
				pagination.size());
		PageResult<TransactionResponse> responses = new PageResult<>(transactionMapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());

		return ResponseEntity.ok(ApiResponse.success(responses));
	}
}
