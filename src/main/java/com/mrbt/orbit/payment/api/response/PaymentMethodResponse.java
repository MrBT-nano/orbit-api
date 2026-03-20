package com.mrbt.orbit.payment.api.response;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record PaymentMethodResponse(UUID id, UUID userId, String provider, String providerReferenceId,
		String lastFourDigits, Boolean isDefault, String status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
