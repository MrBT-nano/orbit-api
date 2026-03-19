package com.mrbt.orbit.ledger.api.request;

import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CreateRecurringTransactionRequest(@NotNull(message = "User ID is required") UUID userId,
		@NotNull(message = "Account ID is required") UUID accountId, UUID categoryId,
		@NotNull(message = "Amount is required") BigDecimal amount,
		@NotBlank(message = "Currency code is required") String currencyCode, String description,
		@NotNull(message = "Frequency is required") Frequency frequency,
		@NotNull(message = "Start date is required") LocalDate startDate, Boolean autoConfirm) {
}
