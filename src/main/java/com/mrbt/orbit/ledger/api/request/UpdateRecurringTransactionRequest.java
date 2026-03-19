package com.mrbt.orbit.ledger.api.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record UpdateRecurringTransactionRequest(String description, BigDecimal amount, UUID categoryId) {
}
