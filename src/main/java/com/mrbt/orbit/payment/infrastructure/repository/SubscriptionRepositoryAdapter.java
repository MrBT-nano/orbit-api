package com.mrbt.orbit.payment.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.core.port.out.SubscriptionRepositoryPort;
import com.mrbt.orbit.payment.infrastructure.entity.SubscriptionEntity;
import com.mrbt.orbit.payment.infrastructure.mapper.SubscriptionEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {

	private final SubscriptionRepository subscriptionRepository;

	private final SubscriptionEntityMapper mapper;

	@Override
	public Subscription save(Subscription subscription) {
		SubscriptionEntity entity = mapper.toEntity(subscription);
		SubscriptionEntity saved = subscriptionRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<Subscription> findById(UUID id) {
		return subscriptionRepository.findByIdAndDeletedAtIsNull(id).map(mapper::toDomain);
	}

	@Override
	public PageResult<Subscription> findByUserId(UUID userId, int page, int size) {
		Page<SubscriptionEntity> entityPage = subscriptionRepository
				.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
		return new PageResult<>(mapper.toDomainList(entityPage.getContent()), entityPage.getTotalElements(),
				entityPage.getTotalPages(), entityPage.getNumber(), entityPage.getSize());
	}

	@Override
	public List<Subscription> findActiveWithUpcomingBilling(LocalDate reminderDate) {
		List<SubscriptionEntity> entities = subscriptionRepository
				.findByStatusAndDeletedAtIsNullAndReminderDaysBeforeIsNotNullAndNextBillingDateIsNotNull(
						SubscriptionStatus.ACTIVE);

		return entities.stream().filter(entity -> {
			LocalDate reminderTriggerDate = entity.getNextBillingDate().minusDays(entity.getReminderDaysBefore());
			return !reminderTriggerDate.isAfter(reminderDate) && entity.getNextBillingDate().isAfter(reminderDate);
		}).map(mapper::toDomain).toList();
	}

	@Override
	public List<Subscription> findActiveWithBillingDue(LocalDate date) {
		return mapper.toDomainList(subscriptionRepository
				.findByStatusAndDeletedAtIsNullAndNextBillingDateLessThanEqual(SubscriptionStatus.ACTIVE, date));
	}

	@Override
	public void softDelete(UUID id) {
		subscriptionRepository.findByIdAndDeletedAtIsNull(id).ifPresent(entity -> {
			entity.softDelete();
			subscriptionRepository.save(entity);
		});
	}

}
