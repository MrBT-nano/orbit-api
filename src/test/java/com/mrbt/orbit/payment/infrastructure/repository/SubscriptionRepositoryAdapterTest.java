package com.mrbt.orbit.payment.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.infrastructure.entity.SubscriptionEntity;
import com.mrbt.orbit.payment.infrastructure.mapper.SubscriptionEntityMapper;

@ExtendWith(MockitoExtension.class)
class SubscriptionRepositoryAdapterTest {

	@Mock
	private SubscriptionRepository subscriptionRepository;

	@Mock
	private SubscriptionEntityMapper mapper;

	@InjectMocks
	private SubscriptionRepositoryAdapter adapter;

	@Test
	void save_convertsAndPersists() {
		Subscription domain = Subscription.builder().name("Netflix").build();
		SubscriptionEntity entity = new SubscriptionEntity();
		SubscriptionEntity savedEntity = new SubscriptionEntity();
		Subscription expected = Subscription.builder().id(UUID.randomUUID()).name("Netflix").build();

		when(mapper.toEntity(domain)).thenReturn(entity);
		when(subscriptionRepository.save(entity)).thenReturn(savedEntity);
		when(mapper.toDomain(savedEntity)).thenReturn(expected);

		Subscription result = adapter.save(domain);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void findById_returnsSubscription() {
		UUID id = UUID.randomUUID();
		SubscriptionEntity entity = new SubscriptionEntity();
		Subscription expected = Subscription.builder().id(id).name("Found").build();

		when(subscriptionRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(expected);

		Optional<Subscription> result = adapter.findById(id);

		assertThat(result).isPresent().contains(expected);
	}

	@Test
	void findById_returnsEmptyWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(subscriptionRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

		assertThat(adapter.findById(id)).isEmpty();
	}

	@Test
	void findByUserId_returnsPageResult() {
		UUID userId = UUID.randomUUID();
		SubscriptionEntity entity = new SubscriptionEntity();
		Subscription mapped = Subscription.builder().name("Sub1").build();
		Page<SubscriptionEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);

		when(subscriptionRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(eq(userId), any()))
				.thenReturn(page);
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		PageResult<Subscription> result = adapter.findByUserId(userId, 0, 10);

		assertThat(result.content()).hasSize(1).first().isEqualTo(mapped);
		assertThat(result.totalElements()).isEqualTo(1);
	}

	@Test
	void findActiveWithUpcomingBilling_filtersCorrectly() {
		LocalDate reminderDate = LocalDate.of(2026, 3, 28);

		SubscriptionEntity match = new SubscriptionEntity();
		match.setNextBillingDate(LocalDate.of(2026, 3, 30));
		match.setReminderDaysBefore(3);
		match.setStatus(SubscriptionStatus.ACTIVE);

		SubscriptionEntity noMatch = new SubscriptionEntity();
		noMatch.setNextBillingDate(LocalDate.of(2026, 4, 15));
		noMatch.setReminderDaysBefore(3);
		noMatch.setStatus(SubscriptionStatus.ACTIVE);

		Subscription matchDomain = Subscription.builder().name("Match").build();

		when(subscriptionRepository
				.findByStatusAndDeletedAtIsNullAndReminderDaysBeforeIsNotNullAndNextBillingDateIsNotNull(
						SubscriptionStatus.ACTIVE))
				.thenReturn(List.of(match, noMatch));
		when(mapper.toDomain(match)).thenReturn(matchDomain);

		List<Subscription> result = adapter.findActiveWithUpcomingBilling(reminderDate);

		assertThat(result).hasSize(1).first().isEqualTo(matchDomain);
	}

	@Test
	void findActiveWithBillingDue_delegatesToRepo() {
		LocalDate date = LocalDate.of(2026, 3, 23);
		SubscriptionEntity entity = new SubscriptionEntity();
		Subscription mapped = Subscription.builder().name("Due").build();

		when(subscriptionRepository
				.findByStatusAndDeletedAtIsNullAndNextBillingDateLessThanEqual(SubscriptionStatus.ACTIVE, date))
				.thenReturn(List.of(entity));
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		List<Subscription> result = adapter.findActiveWithBillingDue(date);

		assertThat(result).hasSize(1).first().isEqualTo(mapped);
	}

	@Test
	void softDelete_deletesWhenFound() {
		UUID id = UUID.randomUUID();
		SubscriptionEntity entity = new SubscriptionEntity();

		when(subscriptionRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(entity));

		adapter.softDelete(id);

		verify(subscriptionRepository).save(entity);
	}

	@Test
	void softDelete_doesNothingWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(subscriptionRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

		adapter.softDelete(id);
	}

}
