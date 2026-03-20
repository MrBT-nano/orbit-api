package com.mrbt.orbit.payment.api.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record UpdateSubscriptionRequest(String name, BigDecimal amount, Integer reminderDaysBefore) {
}
