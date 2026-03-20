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
import com.mrbt.orbit.payment.api.mapper.PaymentMethodDtoMapper;
import com.mrbt.orbit.payment.api.request.CreatePaymentMethodRequest;
import com.mrbt.orbit.payment.api.request.UpdatePaymentMethodRequest;
import com.mrbt.orbit.payment.api.response.PaymentMethodResponse;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.port.in.CreatePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.DeletePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.GetPaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdatePaymentMethodUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
@Tag(name = "Payment Methods", description = "Payment method management for linking cards, wallets, and bank accounts")
public class PaymentMethodController {

	private final CreatePaymentMethodUseCase createPaymentMethodUseCase;

	private final GetPaymentMethodUseCase getPaymentMethodUseCase;

	private final UpdatePaymentMethodUseCase updatePaymentMethodUseCase;

	private final DeletePaymentMethodUseCase deletePaymentMethodUseCase;

	private final PaymentMethodDtoMapper dtoMapper;

	@PostMapping
	@Operation(summary = "Create a payment method", description = "Registers a new payment method for a user")
	public ResponseEntity<ApiResponse<PaymentMethodResponse>> create(
			@Valid @RequestBody CreatePaymentMethodRequest request) {
		PaymentMethod domain = dtoMapper.toDomain(request);
		PaymentMethod created = createPaymentMethodUseCase.create(domain);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Payment method created", dtoMapper.toResponse(created)));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get payment method by ID", description = "Retrieves a specific payment method by its ID")
	public ResponseEntity<ApiResponse<PaymentMethodResponse>> getById(@PathVariable UUID id) {
		PaymentMethod paymentMethod = getPaymentMethodUseCase.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "ID", id));
		return ResponseEntity.ok(ApiResponse.success(dtoMapper.toResponse(paymentMethod)));
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get payment methods for a user", description = "Retrieves paginated payment methods belonging to a specific user")
	public ResponseEntity<ApiResponse<PageResult<PaymentMethodResponse>>> getByUserId(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		PageResult<PaymentMethod> result = getPaymentMethodUseCase.findByUserId(userId, pagination.page(),
				pagination.size());
		PageResult<PaymentMethodResponse> responses = new PageResult<>(dtoMapper.toResponseList(result.content()),
				result.totalElements(), result.totalPages(), result.page(), result.size());
		return ResponseEntity.ok(ApiResponse.success(responses));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a payment method", description = "Partially updates payment method fields")
	public ResponseEntity<ApiResponse<PaymentMethodResponse>> update(@PathVariable UUID id,
			@Valid @RequestBody UpdatePaymentMethodRequest request) {
		PaymentMethod updated = updatePaymentMethodUseCase.update(id, request.isDefault());
		return ResponseEntity.ok(ApiResponse.success("Payment method updated", dtoMapper.toResponse(updated)));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a payment method", description = "Soft deletes a payment method")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
		deletePaymentMethodUseCase.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Payment method deleted", null));
	}

}
