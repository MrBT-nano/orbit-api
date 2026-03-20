package com.mrbt.orbit.payment.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.infrastructure.entity.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

	Page<SubscriptionEntity> findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID userId, Pageable pageable);

	Optional<SubscriptionEntity> findByIdAndDeletedAtIsNull(UUID id);

	List<SubscriptionEntity> findByStatusAndDeletedAtIsNullAndReminderDaysBeforeIsNotNullAndNextBillingDateIsNotNull(
			SubscriptionStatus status);

	List<SubscriptionEntity> findByStatusAndDeletedAtIsNullAndNextBillingDateLessThanEqual(SubscriptionStatus status,
			LocalDate date);

}
