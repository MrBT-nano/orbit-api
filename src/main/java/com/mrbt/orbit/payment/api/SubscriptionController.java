package com.mrbt.orbit.payment.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.api.PaginationParams;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.payment.api.mapper.SubscriptionDtoMapper;
import com.mrbt.orbit.payment.api.request.CreateSubscriptionRequest;
import com.mrbt.orbit.payment.api.request.UpdateSubscriptionRequest;
import com.mrbt.orbit.payment.api.response.SubscriptionResponse;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.port.in.CreateSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.DeleteSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.GetSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdateSubscriptionUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Subscription tracking and recurring billing management")
public class SubscriptionController {

	private final CreateSubscriptionUseCase createSubscriptionUseCase;

	private final GetSubscriptionUseCase getSubscriptionUseCase;

	private final UpdateSubscriptionUseCase updateSubscriptionUseCase;

	private final DeleteSubscriptionUseCase deleteSubscriptionUseCase;

	private final SubscriptionDtoMapper dtoMapper;

	@PostMapping
	@Operation(summary = "Create a subscription", description = "Registers a new subscription for tracking recurring payments")
	public ResponseEntity<ApiResponse<SubscriptionResponse>> create(
			@Valid @RequestBody CreateSubscriptionRequest request) {
		Subscription domain = dtoMapper.toDomain(request);
		Subscription created = createSubscriptionUseCase.create(domain);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Subscription created", dtoMapper.toResponse(created)));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get subscription by ID", description = "Retrieves a specific subscription by its ID")
	public ResponseEntity<ApiResponse<SubscriptionResponse>> getById(@PathVariable UUID id) {
		Subscription subscription = getSubscriptionUseCase.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription", "ID", id));
		return ResponseEntity.ok(ApiResponse.success(dtoMapper.toResponse(subscription)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get subscriptions for a user", description = "Retrieves paginated subscriptions belonging to a specific user")
	public ResponseEntity<ApiResponse<PageResult<SubscriptionResponse>>> getByUserId(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<Subscription> result = getSubscriptionUseCase.findByUserId(userId, pagination.page(),
				pagination.size());
		PageResult<SubscriptionResponse> responses = new PageResult<>(dtoMapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());
		return ResponseEntity.ok(ApiResponse.success(responses));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a subscription", description = "Partially updates subscription fields")
	public ResponseEntity<ApiResponse<SubscriptionResponse>> update(@PathVariable UUID id,
			@Valid @RequestBody UpdateSubscriptionRequest request) {
		Subscription updated = updateSubscriptionUseCase.update(id, request.name(), request.amount(),
				request.reminderDaysBefore());
		return ResponseEntity.ok(ApiResponse.success("Subscription updated", dtoMapper.toResponse(updated)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Cancel a subscription", description = "Cancels and soft deletes a subscription")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
		deleteSubscriptionUseCase.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Subscription cancelled", null));
	}

	@PatchMapping("/{id}/pause")
	@Operation(summary = "Toggle subscription pause", description = "Pauses an active subscription or resumes a paused one")
	public ResponseEntity<ApiResponse<SubscriptionResponse>> togglePause(@PathVariable UUID id) {
		Subscription toggled = updateSubscriptionUseCase.togglePause(id);
		return ResponseEntity.ok(ApiResponse.success("Subscription pause toggled", dtoMapper.toResponse(toggled)));
	}

}
