package com.mrbt.orbit.budget.api;

import com.mrbt.orbit.budget.api.mapper.GoalDtoMapper;
import com.mrbt.orbit.budget.api.request.ContributeGoalRequest;
import com.mrbt.orbit.budget.api.request.CreateGoalRequest;
import com.mrbt.orbit.budget.api.response.GoalResponse;
import com.mrbt.orbit.budget.core.model.Goal;
import com.mrbt.orbit.budget.core.port.in.ContributeGoalUseCase;
import com.mrbt.orbit.budget.core.port.in.CreateGoalUseCase;
import com.mrbt.orbit.budget.core.port.in.GetGoalUseCase;
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
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Savings goal tracking")
public class GoalController {

	private final CreateGoalUseCase createGoalUseCase;
	private final GetGoalUseCase getGoalUseCase;
	private final ContributeGoalUseCase contributeGoalUseCase;
	private final GoalDtoMapper dtoMapper;

	@PostMapping
	@Operation(summary = "Create a new goal", description = "Creates a savings goal, optionally linked to an account")
	public ResponseEntity<ApiResponse<GoalResponse>> createGoal(@Valid @RequestBody CreateGoalRequest request) {
		Goal goal = dtoMapper.toDomain(request);
		Goal created = createGoalUseCase.createGoal(goal);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Goal created successfully", dtoMapper.toResponse(created)));
	}

	@GetMapping("/{goalId}")
	@Operation(summary = "Get goal by ID", description = "Retrieves a goal, resolving current amount from linked account if applicable")
	public ResponseEntity<ApiResponse<GoalResponse>> getGoalById(@PathVariable UUID goalId) {
		Goal goal = getGoalUseCase.getGoalById(goalId)
				.orElseThrow(() -> new ResourceNotFoundException("Goal", "ID", goalId));
		return ResponseEntity.ok(ApiResponse.success(dtoMapper.toResponse(goal)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get goals for a user", description = "Returns paginated goals for a user")
	public ResponseEntity<ApiResponse<PageResult<GoalResponse>>> getGoalsByUserId(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<Goal> result = getGoalUseCase.getGoalsByUserId(userId, pagination.page(), pagination.size());
		PageResult<GoalResponse> responses = new PageResult<>(dtoMapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());
		return ResponseEntity.ok(ApiResponse.success(responses));
	}

	@PatchMapping("/{goalId}/contribute")
	@Operation(summary = "Contribute to a goal", description = "Adds a manual contribution to a non-linked goal")
	public ResponseEntity<ApiResponse<GoalResponse>> contribute(@PathVariable UUID goalId,
			@Valid @RequestBody ContributeGoalRequest request) {
		Goal goal = contributeGoalUseCase.contribute(goalId, request.amount());
		return ResponseEntity.ok(ApiResponse.success("Contribution recorded", dtoMapper.toResponse(goal)));
	}
}
