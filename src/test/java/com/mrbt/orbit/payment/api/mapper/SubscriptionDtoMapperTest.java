package com.mrbt.orbit.payment.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.payment.api.request.CreateSubscriptionRequest;
import com.mrbt.orbit.payment.api.response.SubscriptionResponse;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;

class SubscriptionDtoMapperTest {

	private final SubscriptionDtoMapper mapper = new SubscriptionDtoMapper();

	@Test
	void toDomain_fromRequest_mapsAllFields() {
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();
		CreateSubscriptionRequest request = CreateSubscriptionRequest.builder().userId(userId).accountId(accountId)
				.name("Netflix").amount(new BigDecimal("399.00")).currencyCode("THB").billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(LocalDate.of(2026, 4, 1)).reminderDaysBefore(3).trialEndDate(LocalDate.of(2026, 3, 31))
				.build();

		Subscription result = mapper.toDomain(request);

		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccountId()).isEqualTo(accountId);
		assertThat(result.getName()).isEqualTo("Netflix");
		assertThat(result.getAmount()).isEqualByComparingTo("399.00");
		assertThat(result.getBillingCycle()).isEqualTo(BillingCycle.MONTHLY);
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void toResponse_mapsAllFields() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		Subscription domain = Subscription.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).name("Spotify")
				.amount(new BigDecimal("149.00")).billingCycle(BillingCycle.MONTHLY).status(SubscriptionStatus.ACTIVE)
				.createdAt(now).updatedAt(now).build();

		SubscriptionResponse result = mapper.toResponse(domain);

		assertThat(result.name()).isEqualTo("Spotify");
		assertThat(result.billingCycle()).isEqualTo("MONTHLY");
		assertThat(result.status()).isEqualTo("ACTIVE");
	}

	@Test
	void toResponse_returnsNullForNull() {
		assertThat(mapper.toResponse(null)).isNull();
	}

	@Test
	void toResponse_handlesNullEnums() {
		Subscription domain = Subscription.builder().name("Minimal").build();

		SubscriptionResponse result = mapper.toResponse(domain);

		assertThat(result.billingCycle()).isNull();
		assertThat(result.status()).isNull();
	}

	@Test
	void toResponseList_convertsList() {
		List<Subscription> domains = List.of(
				Subscription.builder().name("A").billingCycle(BillingCycle.MONTHLY).status(SubscriptionStatus.ACTIVE)
						.build(),
				Subscription.builder().name("B").billingCycle(BillingCycle.YEARLY).status(SubscriptionStatus.PAUSED)
						.build());

		List<SubscriptionResponse> result = mapper.toResponseList(domains);

		assertThat(result).hasSize(2);
		assertThat(result.get(0).name()).isEqualTo("A");
	}

	@Test
	void toResponseList_returnsNullForNull() {
		assertThat(mapper.toResponseList(null)).isNull();
	}

}
