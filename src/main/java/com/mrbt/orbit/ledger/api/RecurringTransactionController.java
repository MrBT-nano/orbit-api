package com.mrbt.orbit.ledger.api;

import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.api.PaginationParams;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.api.request.CreateRecurringTransactionRequest;
import com.mrbt.orbit.ledger.api.request.UpdateRecurringTransactionRequest;
import com.mrbt.orbit.ledger.api.response.RecurringTransactionResponse;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.port.in.CreateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.DeleteRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.GetRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.UpdateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.infrastructure.mapper.RecurringTransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recurring-transactions")
@RequiredArgsConstructor
@Tag(name = "Recurring Transactions", description = "Manage recurring transaction rules and schedules")
public class RecurringTransactionController {

	private final CreateRecurringTransactionUseCase createUseCase;
	private final GetRecurringTransactionUseCase getUseCase;
	private final UpdateRecurringTransactionUseCase updateUseCase;
	private final DeleteRecurringTransactionUseCase deleteUseCase;
	private final RecurringTransactionMapper mapper;

	@PostMapping
	@Operation(summary = "Create recurring transaction rule", description = "Creates a new recurring transaction schedule")
	public ResponseEntity<ApiResponse<RecurringTransactionResponse>> create(
			@Valid @RequestBody CreateRecurringTransactionRequest request) {
		RecurringTransaction recurring = RecurringTransaction.builder().userId(request.userId())
				.accountId(request.accountId()).categoryId(request.categoryId()).amount(request.amount())
				.currencyCode(request.currencyCode()).description(request.description()).frequency(request.frequency())
				.nextOccurrence(request.startDate()).autoConfirm(request.autoConfirm()).build();
		RecurringTransaction created = createUseCase.create(recurring);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Recurring transaction created", mapper.toResponse(created)));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get recurring transaction by ID", description = "Retrieves a recurring transaction rule")
	public ResponseEntity<ApiResponse<RecurringTransactionResponse>> getById(@PathVariable UUID id) {
		RecurringTransaction recurring = getUseCase.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("RecurringTransaction", "ID", id));
		return ResponseEntity.ok(ApiResponse.success(mapper.toResponse(recurring)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get recurring transactions for a user", description = "Returns paginated recurring transaction rules")
	public ResponseEntity<ApiResponse<PageResult<RecurringTransactionResponse>>> getByUserId(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<RecurringTransaction> result = getUseCase.findByUserId(userId, pagination.page(), pagination.size());
		PageResult<RecurringTransactionResponse> responses = new PageResult<>(mapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());
		return ResponseEntity.ok(ApiResponse.success(responses));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update recurring transaction", description = "Partially updates a recurring transaction rule")
	public ResponseEntity<ApiResponse<RecurringTransactionResponse>> update(@PathVariable UUID id,
			@Valid @RequestBody UpdateRecurringTransactionRequest request) {
		RecurringTransaction updated = updateUseCase.update(id, request.description(), request.amount(),
				request.categoryId());
		return ResponseEntity.ok(ApiResponse.success("Recurring transaction updated", mapper.toResponse(updated)));
	}

	@PatchMapping("/{id}/pause")
	@Operation(summary = "Toggle pause/resume", description = "Toggles a recurring transaction between ACTIVE and PAUSED")
	public ResponseEntity<ApiResponse<RecurringTransactionResponse>> togglePause(@PathVariable UUID id) {
		RecurringTransaction updated = updateUseCase.togglePause(id);
		return ResponseEntity.ok(ApiResponse.success("Status toggled", mapper.toResponse(updated)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Cancel recurring transaction", description = "Soft deletes by setting status to CANCELLED")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
		deleteUseCase.cancel(id);
		return ResponseEntity.ok(ApiResponse.success("Recurring transaction cancelled", null));
	}
}
