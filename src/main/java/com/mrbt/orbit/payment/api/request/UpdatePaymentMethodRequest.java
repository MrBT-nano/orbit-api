package com.mrbt.orbit.payment.api.request;

import lombok.Builder;

@Builder
public record UpdatePaymentMethodRequest(Boolean isDefault) {
}
