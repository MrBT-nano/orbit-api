package com.mrbt.orbit.payment.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record SubscriptionResponse(UUID id, UUID userId, UUID categoryId, UUID paymentMethodId, UUID accountId,
		String name, BigDecimal amount, String currencyCode, String billingCycle, LocalDate nextBillingDate,
		Integer reminderDaysBefore, String status, LocalDate trialEndDate, OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
