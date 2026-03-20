package com.mrbt.orbit.payment.api.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.mrbt.orbit.payment.core.model.enums.BillingCycle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateSubscriptionRequest(@NotNull(message = "User ID is required") UUID userId,

		@NotNull(message = "Account ID is required") UUID accountId,

		UUID categoryId,

		UUID paymentMethodId,

		@NotBlank(message = "Name is required") String name,

		@NotNull(message = "Amount is required") BigDecimal amount,

		@NotBlank(message = "Currency code is required") String currencyCode,

		@NotNull(message = "Billing cycle is required") BillingCycle billingCycle,

		@NotNull(message = "Next billing date is required") LocalDate nextBillingDate,

		Integer reminderDaysBefore,

		LocalDate trialEndDate) {
}
