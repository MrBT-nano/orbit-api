package com.mrbt.orbit.payment.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.BaseDomainModel;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Subscription extends BaseDomainModel {

	private UUID userId;

	private UUID categoryId;

	private UUID paymentMethodId;

	private UUID accountId;

	private String name;

	private BigDecimal amount;

	private String currencyCode;

	private BillingCycle billingCycle;

	private LocalDate nextBillingDate;

	private Integer reminderDaysBefore;

	private SubscriptionStatus status;

	private LocalDate trialEndDate;

}
