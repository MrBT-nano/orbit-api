package com.mrbt.orbit.payment.infrastructure.mapper;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.common.infrastructure.mapper.AbstractNullSafeMapper;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.infrastructure.entity.SubscriptionEntity;

@Component
public class SubscriptionEntityMapper extends AbstractNullSafeMapper<SubscriptionEntity, Subscription> {

	@Override
	public Subscription toDomain(SubscriptionEntity entity) {
		if (entity == null) {
			return null;
		}

		return Subscription.builder().id(entity.getId()).userId(entity.getUserId()).categoryId(entity.getCategoryId())
				.paymentMethodId(entity.getPaymentMethodId()).accountId(entity.getAccountId()).name(entity.getName())
				.amount(entity.getAmount()).currencyCode(entity.getCurrencyCode())
				.billingCycle(entity.getBillingCycle()).nextBillingDate(entity.getNextBillingDate())
				.reminderDaysBefore(entity.getReminderDaysBefore()).status(entity.getStatus())
				.trialEndDate(entity.getTrialEndDate())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	@Override
	public SubscriptionEntity toEntity(Subscription domain) {
		if (domain == null) {
			return null;
		}

		SubscriptionEntity entity = new SubscriptionEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setCategoryId(domain.getCategoryId());
		entity.setPaymentMethodId(domain.getPaymentMethodId());
		entity.setAccountId(domain.getAccountId());
		entity.setName(domain.getName());
		entity.setAmount(domain.getAmount());
		entity.setCurrencyCode(domain.getCurrencyCode());
		entity.setBillingCycle(domain.getBillingCycle());
		entity.setNextBillingDate(domain.getNextBillingDate());
		entity.setReminderDaysBefore(domain.getReminderDaysBefore());
		entity.setStatus(domain.getStatus());
		entity.setTrialEndDate(domain.getTrialEndDate());
		return entity;
	}

}
