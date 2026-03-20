package com.mrbt.orbit.payment.core.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.Subscription;

public interface SubscriptionRepositoryPort {

	Subscription save(Subscription subscription);

	Optional<Subscription> findById(UUID id);

	PageResult<Subscription> findByUserId(UUID userId, int page, int size);

	List<Subscription> findActiveWithUpcomingBilling(LocalDate reminderDate);

	List<Subscription> findActiveWithBillingDue(LocalDate date);

	void softDelete(UUID id);

}
