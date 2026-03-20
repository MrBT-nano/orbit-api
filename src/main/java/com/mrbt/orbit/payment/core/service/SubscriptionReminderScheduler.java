package com.mrbt.orbit.payment.core.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.core.port.in.CreateNotificationUseCase;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.port.out.SubscriptionRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionReminderScheduler {

	private final SubscriptionRepositoryPort subscriptionRepo;

	private final CreateNotificationUseCase createNotificationUseCase;

	@Scheduled(cron = "0 0 8 * * *")
	@Transactional
	public void processReminders() {
		LocalDate today = LocalDate.now();

		List<Subscription> upcoming = subscriptionRepo.findActiveWithUpcomingBilling(today);
		for (Subscription sub : upcoming) {
			long daysUntilBilling = ChronoUnit.DAYS.between(today, sub.getNextBillingDate());
			createNotificationUseCase.createNotification(Notification.builder().userId(sub.getUserId())
					.type(NotificationType.BILL_REMINDER).title("Upcoming Bill")
					.message(String.format("Your subscription %s (%s %s) will be billed in %d days", sub.getName(),
							sub.getAmount(), sub.getCurrencyCode(), daysUntilBilling))
					.channel(NotificationChannel.IN_APP).isRead(false).build());
		}

		List<Subscription> due = subscriptionRepo.findActiveWithBillingDue(today);
		for (Subscription sub : due) {
			sub.setNextBillingDate(advanceDate(sub.getNextBillingDate(), sub.getBillingCycle()));
			subscriptionRepo.save(sub);
		}

		log.info("Processed {} reminders, advanced {} billing dates", upcoming.size(), due.size());
	}

	private LocalDate advanceDate(LocalDate current, BillingCycle cycle) {
		return switch (cycle) {
			case WEEKLY -> current.plusWeeks(1);
			case MONTHLY -> current.plusMonths(1);
			case QUARTERLY -> current.plusMonths(3);
			case YEARLY -> current.plusYears(1);
		};
	}

}
