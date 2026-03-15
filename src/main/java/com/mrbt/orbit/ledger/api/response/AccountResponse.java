package com.mrbt.orbit.ledger.api.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record AccountResponse(UUID id, UUID userId, String name, String type, String currencyCode,
		BigDecimal currentBalance, String plaidAccountId, String status, OffsetDateTime createdAt,
		OffsetDateTime updatedAt) {
}
