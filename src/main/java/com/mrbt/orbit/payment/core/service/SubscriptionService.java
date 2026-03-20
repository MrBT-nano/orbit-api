package com.mrbt.orbit.payment.core.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.core.port.in.CreateSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.DeleteSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.GetSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdateSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.out.SubscriptionRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService
		implements
			CreateSubscriptionUseCase,
			GetSubscriptionUseCase,
			UpdateSubscriptionUseCase,
			DeleteSubscriptionUseCase {

	private final SubscriptionRepositoryPort repositoryPort;

	@Override
	@Transactional
	public Subscription create(Subscription subscription) {
		if (subscription.getStatus() == null) {
			subscription.setStatus(SubscriptionStatus.ACTIVE);
		}
		return repositoryPort.save(subscription);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Subscription> findById(UUID id) {
		return repositoryPort.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResult<Subscription> findByUserId(UUID userId, int page, int size) {
		return repositoryPort.findByUserId(userId, page, size);
	}

	@Override
	@Transactional
	public Subscription update(UUID id, String name, BigDecimal amount, Integer reminderDaysBefore) {
		Subscription subscription = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription", "ID", id));
		if (name != null) {
			subscription.setName(name);
		}
		if (amount != null) {
			subscription.setAmount(amount);
		}
		if (reminderDaysBefore != null) {
			subscription.setReminderDaysBefore(reminderDaysBefore);
		}
		return repositoryPort.save(subscription);
	}

	@Override
	@Transactional
	public Subscription togglePause(UUID id) {
		Subscription subscription = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription", "ID", id));
		if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
			subscription.setStatus(SubscriptionStatus.PAUSED);
		} else if (subscription.getStatus() == SubscriptionStatus.PAUSED) {
			subscription.setStatus(SubscriptionStatus.ACTIVE);
		}
		return repositoryPort.save(subscription);
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		Subscription subscription = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription", "ID", id));
		subscription.setStatus(SubscriptionStatus.CANCELLED);
		repositoryPort.save(subscription);
		repositoryPort.softDelete(id);
	}

}
