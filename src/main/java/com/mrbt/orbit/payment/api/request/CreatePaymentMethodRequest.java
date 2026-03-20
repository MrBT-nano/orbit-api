package com.mrbt.orbit.payment.api.request;

import java.util.UUID;

import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreatePaymentMethodRequest(@NotNull(message = "User ID is required") UUID userId,

		@NotNull(message = "Provider is required") PaymentProvider provider,

		String providerReferenceId,

		String lastFourDigits,

		Boolean isDefault) {
}
