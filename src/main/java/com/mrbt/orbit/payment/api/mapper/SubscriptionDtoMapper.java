package com.mrbt.orbit.payment.api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.payment.api.request.CreateSubscriptionRequest;
import com.mrbt.orbit.payment.api.response.SubscriptionResponse;
import com.mrbt.orbit.payment.core.model.Subscription;

@Component
public class SubscriptionDtoMapper {

	public Subscription toDomain(CreateSubscriptionRequest request) {
		if (request == null)
			return null;

		return Subscription.builder().userId(request.userId()).accountId(request.accountId())
				.categoryId(request.categoryId()).paymentMethodId(request.paymentMethodId()).name(request.name())
				.amount(request.amount()).currencyCode(request.currencyCode()).billingCycle(request.billingCycle())
				.nextBillingDate(request.nextBillingDate()).reminderDaysBefore(request.reminderDaysBefore())
				.trialEndDate(request.trialEndDate()).build();
	}

	public SubscriptionResponse toResponse(Subscription domain) {
		if (domain == null)
			return null;

		return SubscriptionResponse.builder().id(domain.getId()).userId(domain.getUserId())
				.categoryId(domain.getCategoryId()).paymentMethodId(domain.getPaymentMethodId())
				.accountId(domain.getAccountId()).name(domain.getName()).amount(domain.getAmount())
				.currencyCode(domain.getCurrencyCode())
				.billingCycle(domain.getBillingCycle() != null ? domain.getBillingCycle().name() : null)
				.nextBillingDate(domain.getNextBillingDate()).reminderDaysBefore(domain.getReminderDaysBefore())
				.status(domain.getStatus() != null ? domain.getStatus().name() : null)
				.trialEndDate(domain.getTrialEndDate()).createdAt(domain.getCreatedAt())
				.updatedAt(domain.getUpdatedAt()).build();
	}

	public List<SubscriptionResponse> toResponseList(List<Subscription> domains) {
		if (domains == null)
			return null;
		return domains.stream().map(this::toResponse).toList();
	}

}
