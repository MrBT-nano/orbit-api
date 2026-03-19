package com.mrbt.orbit.budget.api;

import com.mrbt.orbit.budget.api.mapper.BudgetDtoMapper;
import com.mrbt.orbit.budget.api.request.CreateBudgetRequest;
import com.mrbt.orbit.budget.api.response.BudgetResponse;
import com.mrbt.orbit.budget.core.model.Budget;
import com.mrbt.orbit.budget.core.port.in.ArchiveBudgetUseCase;
import com.mrbt.orbit.budget.core.port.in.CreateBudgetUseCase;
import com.mrbt.orbit.budget.core.port.in.GetBudgetUseCase;
import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.api.PaginationParams;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgets", description = "Budget management for tracking spending limits")
public class BudgetController {

	private final CreateBudgetUseCase createBudgetUseCase;
	private final GetBudgetUseCase getBudgetUseCase;
	private final ArchiveBudgetUseCase archiveBudgetUseCase;
	private final BudgetDtoMapper dtoMapper;

	@PostMapping
	@Operation(summary = "Create a new budget", description = "Creates a budget with allocated items per category")
	public ResponseEntity<ApiResponse<BudgetResponse>> createBudget(@Valid @RequestBody CreateBudgetRequest request) {
		Budget budget = dtoMapper.toDomain(request);
		Budget created = createBudgetUseCase.createBudget(budget);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Budget created successfully", dtoMapper.toResponse(created)));
	}

	@GetMapping("/{budgetId}")
	@Operation(summary = "Get budget by ID", description = "Retrieves a budget with its items")
	public ResponseEntity<ApiResponse<BudgetResponse>> getBudgetById(@PathVariable UUID budgetId) {
		Budget budget = getBudgetUseCase.getBudgetById(budgetId)
				.orElseThrow(() -> new ResourceNotFoundException("Budget", "ID", budgetId));
		return ResponseEntity.ok(ApiResponse.success(dtoMapper.toResponse(budget)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get budgets for a user", description = "Returns paginated budgets for a user")
	public ResponseEntity<ApiResponse<PageResult<BudgetResponse>>> getBudgetsByUserId(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<Budget> result = getBudgetUseCase.getBudgetsByUserId(userId, pagination.page(), pagination.size());
		PageResult<BudgetResponse> responses = new PageResult<>(dtoMapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());
		return ResponseEntity.ok(ApiResponse.success(responses));
	}

	@PatchMapping("/{budgetId}/archive")
	@Operation(summary = "Archive a budget", description = "Sets budget status to ARCHIVED")
	public ResponseEntity<ApiResponse<Void>> archiveBudget(@PathVariable UUID budgetId) {
		archiveBudgetUseCase.archiveBudget(budgetId);
		return ResponseEntity.ok(ApiResponse.success("Budget archived", null));
	}
}
