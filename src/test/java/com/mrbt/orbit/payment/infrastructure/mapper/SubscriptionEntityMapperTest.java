package com.mrbt.orbit.payment.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.infrastructure.entity.SubscriptionEntity;

class SubscriptionEntityMapperTest {

	private final SubscriptionEntityMapper mapper = new SubscriptionEntityMapper();

	@Test
	void toDomain_mapsAllFields() {
		SubscriptionEntity entity = new SubscriptionEntity();
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();
		UUID categoryId = UUID.randomUUID();
		UUID paymentMethodId = UUID.randomUUID();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setCategoryId(categoryId);
		entity.setPaymentMethodId(paymentMethodId);
		entity.setAccountId(accountId);
		entity.setName("Netflix");
		entity.setAmount(new BigDecimal("399.00"));
		entity.setCurrencyCode("THB");
		entity.setBillingCycle(BillingCycle.MONTHLY);
		entity.setNextBillingDate(LocalDate.of(2026, 4, 1));
		entity.setReminderDaysBefore(3);
		entity.setStatus(SubscriptionStatus.ACTIVE);
		entity.setTrialEndDate(LocalDate.of(2026, 3, 31));
		entity.setCreatedAt(Instant.parse("2026-01-01T00:00:00Z"));
		entity.setUpdatedAt(Instant.parse("2026-01-02T00:00:00Z"));

		Subscription result = mapper.toDomain(entity);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getCategoryId()).isEqualTo(categoryId);
		assertThat(result.getPaymentMethodId()).isEqualTo(paymentMethodId);
		assertThat(result.getAccountId()).isEqualTo(accountId);
		assertThat(result.getName()).isEqualTo("Netflix");
		assertThat(result.getAmount()).isEqualByComparingTo("399.00");
		assertThat(result.getCurrencyCode()).isEqualTo("THB");
		assertThat(result.getBillingCycle()).isEqualTo(BillingCycle.MONTHLY);
		assertThat(result.getNextBillingDate()).isEqualTo(LocalDate.of(2026, 4, 1));
		assertThat(result.getReminderDaysBefore()).isEqualTo(3);
		assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
		assertThat(result.getTrialEndDate()).isEqualTo(LocalDate.of(2026, 3, 31));
		assertThat(result.getCreatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
		assertThat(result.getUpdatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain((SubscriptionEntity) null)).isNull();
	}

	@Test
	void toDomain_handlesNullTimestamps() {
		SubscriptionEntity entity = new SubscriptionEntity();
		entity.setName("Test");
		entity.setBillingCycle(BillingCycle.YEARLY);
		entity.setStatus(SubscriptionStatus.ACTIVE);

		Subscription result = mapper.toDomain(entity);

		assertThat(result.getCreatedAt()).isNull();
		assertThat(result.getUpdatedAt()).isNull();
	}

	@Test
	void toEntity_mapsAllFields() {
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();
		Subscription domain = Subscription.builder().id(id).userId(userId).accountId(accountId).name("Spotify")
				.amount(new BigDecimal("149.00")).currencyCode("THB").billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(LocalDate.of(2026, 5, 1)).reminderDaysBefore(7).status(SubscriptionStatus.ACTIVE)
				.trialEndDate(LocalDate.of(2026, 4, 30)).build();

		SubscriptionEntity result = mapper.toEntity(domain);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccountId()).isEqualTo(accountId);
		assertThat(result.getName()).isEqualTo("Spotify");
		assertThat(result.getAmount()).isEqualByComparingTo("149.00");
		assertThat(result.getBillingCycle()).isEqualTo(BillingCycle.MONTHLY);
		assertThat(result.getNextBillingDate()).isEqualTo(LocalDate.of(2026, 5, 1));
		assertThat(result.getReminderDaysBefore()).isEqualTo(7);
		assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
		assertThat(result.getTrialEndDate()).isEqualTo(LocalDate.of(2026, 4, 30));
	}

	@Test
	void toEntity_returnsNullForNull() {
		assertThat(mapper.toEntity(null)).isNull();
	}

}
