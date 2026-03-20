package com.mrbt.orbit.payment.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.core.port.out.SubscriptionRepositoryPort;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

	@Mock
	private SubscriptionRepositoryPort repositoryPort;

	@InjectMocks
	private SubscriptionService subscriptionService;

	@Test
	void create_setsDefaultStatus() {
		Subscription sub = Subscription.builder().userId(UUID.randomUUID()).name("Netflix")
				.amount(new BigDecimal("15.99")).billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(LocalDate.of(2026, 4, 1)).build();

		when(repositoryPort.save(any(Subscription.class))).thenAnswer(inv -> {
			Subscription saved = inv.getArgument(0);
			saved.setId(UUID.randomUUID());
			return saved;
		});

		Subscription result = subscriptionService.create(sub);

		assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
		verify(repositoryPort).save(sub);
	}

	@Test
	void findById_returnsSubscription() {
		UUID id = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(id).name("Spotify").build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(sub));

		Optional<Subscription> result = subscriptionService.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("Spotify");
	}

	@Test
	void findByUserId_returnsPage() {
		UUID userId = UUID.randomUUID();
		Subscription sub = Subscription.builder().userId(userId).name("Netflix").build();
		PageResult<Subscription> page = new PageResult<>(List.of(sub), 1L, 1, 0, 20);

		when(repositoryPort.findByUserId(userId, 0, 20)).thenReturn(page);

		PageResult<Subscription> result = subscriptionService.findByUserId(userId, 0, 20);

		assertThat(result.content()).hasSize(1);
	}

	@Test
	void update_setsName() {
		UUID id = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(id).name("Netflix").amount(new BigDecimal("15.99")).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(sub));
		when(repositoryPort.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

		Subscription result = subscriptionService.update(id, "Netflix Premium", null, null);

		assertThat(result.getName()).isEqualTo("Netflix Premium");
		verify(repositoryPort).save(sub);
	}

	@Test
	void togglePause_activeToPaused() {
		UUID id = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(id).name("Netflix").status(SubscriptionStatus.ACTIVE).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(sub));
		when(repositoryPort.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

		Subscription result = subscriptionService.togglePause(id);

		assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.PAUSED);
	}

	@Test
	void togglePause_pausedToActive() {
		UUID id = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(id).name("Netflix").status(SubscriptionStatus.PAUSED).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(sub));
		when(repositoryPort.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

		Subscription result = subscriptionService.togglePause(id);

		assertThat(result.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
	}

	@Test
	void delete_setsStatusAndSoftDeletes() {
		UUID id = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(id).name("Netflix").status(SubscriptionStatus.ACTIVE).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(sub));
		when(repositoryPort.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

		subscriptionService.delete(id);

		assertThat(sub.getStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
		verify(repositoryPort).save(sub);
		verify(repositoryPort).softDelete(id);
	}

}
