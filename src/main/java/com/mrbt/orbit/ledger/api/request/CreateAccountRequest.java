package com.mrbt.orbit.ledger.api.request;

import com.mrbt.orbit.ledger.core.model.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CreateAccountRequest(@NotNull(message = "User ID is required") UUID userId,

		@NotBlank(message = "Account name cannot be blank") String name,

		@NotNull(message = "Account type is required") AccountType type,

		@NotBlank(message = "Currency code cannot be blank") String currencyCode,

		BigDecimal initialBalance,

		String plaidAccountId) {
}
