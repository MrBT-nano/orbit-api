package com.mrbt.orbit.ledger.api.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record RecurringTransactionResponse(UUID id, UUID userId, UUID accountId, UUID categoryId, BigDecimal amount,
		String currencyCode, String description, String frequency, LocalDate nextOccurrence, Boolean autoConfirm,
		String status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
}
