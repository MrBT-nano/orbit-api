package com.mrbt.orbit.payment.core.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.port.in.CreateNotificationUseCase;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.port.out.SubscriptionRepositoryPort;

@ExtendWith(MockitoExtension.class)
class SubscriptionReminderSchedulerTest {

	@Mock
	private SubscriptionRepositoryPort subscriptionRepo;

	@Mock
	private CreateNotificationUseCase createNotificationUseCase;

	@InjectMocks
	private SubscriptionReminderScheduler scheduler;

	@Test
	void processReminders_sendsNotification() {
		LocalDate today = LocalDate.now();
		Subscription sub = Subscription.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).name("Netflix")
				.amount(new BigDecimal("15.99")).currencyCode("USD").billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(today.plusDays(3)).build();

		when(subscriptionRepo.findActiveWithUpcomingBilling(today)).thenReturn(List.of(sub));
		when(subscriptionRepo.findActiveWithBillingDue(today)).thenReturn(List.of());

		scheduler.processReminders();

		verify(createNotificationUseCase).createNotification(any(Notification.class));
	}

	@Test
	void processReminders_advancesBillingDate() {
		LocalDate today = LocalDate.now();
		Subscription sub = Subscription.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).name("Spotify")
				.amount(new BigDecimal("9.99")).currencyCode("USD").billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(today).build();

		when(subscriptionRepo.findActiveWithUpcomingBilling(today)).thenReturn(List.of());
		when(subscriptionRepo.findActiveWithBillingDue(today)).thenReturn(List.of(sub));

		scheduler.processReminders();

		verify(subscriptionRepo).save(sub);
	}

	@Test
	void processReminders_doesNothingWhenEmpty() {
		LocalDate today = LocalDate.now();

		when(subscriptionRepo.findActiveWithUpcomingBilling(today)).thenReturn(List.of());
		when(subscriptionRepo.findActiveWithBillingDue(today)).thenReturn(List.of());

		scheduler.processReminders();

		verify(createNotificationUseCase, never()).createNotification(any());
		verify(subscriptionRepo, never()).save(any());
	}

}
