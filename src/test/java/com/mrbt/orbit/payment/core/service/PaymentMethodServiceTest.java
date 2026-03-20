package com.mrbt.orbit.payment.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;
import com.mrbt.orbit.payment.core.port.out.PaymentMethodRepositoryPort;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

	@Mock
	private PaymentMethodRepositoryPort repositoryPort;

	@InjectMocks
	private PaymentMethodService paymentMethodService;

	@Test
	void create_setsDefaultStatusAndIsDefault() {
		PaymentMethod pm = PaymentMethod.builder().userId(UUID.randomUUID()).provider(PaymentProvider.STRIPE).build();

		when(repositoryPort.save(any(PaymentMethod.class))).thenAnswer(inv -> {
			PaymentMethod saved = inv.getArgument(0);
			saved.setId(UUID.randomUUID());
			return saved;
		});

		PaymentMethod result = paymentMethodService.create(pm);

		assertThat(result.getStatus()).isEqualTo(PaymentMethodStatus.ACTIVE);
		assertThat(result.getIsDefault()).isFalse();
		verify(repositoryPort).save(pm);
	}

	@Test
	void findById_returnsPaymentMethod() {
		UUID id = UUID.randomUUID();
		PaymentMethod pm = PaymentMethod.builder().id(id).provider(PaymentProvider.PAYPAL).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(pm));

		Optional<PaymentMethod> result = paymentMethodService.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().getProvider()).isEqualTo(PaymentProvider.PAYPAL);
	}

	@Test
	void findByUserId_returnsPage() {
		UUID userId = UUID.randomUUID();
		PaymentMethod pm = PaymentMethod.builder().userId(userId).provider(PaymentProvider.STRIPE).build();
		PageResult<PaymentMethod> page = new PageResult<>(List.of(pm), 1L, 1, 0, 20);

		when(repositoryPort.findByUserId(userId, 0, 20)).thenReturn(page);

		PageResult<PaymentMethod> result = paymentMethodService.findByUserId(userId, 0, 20);

		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1L);
	}

	@Test
	void update_setsIsDefault() {
		UUID id = UUID.randomUUID();
		PaymentMethod pm = PaymentMethod.builder().id(id).isDefault(false).provider(PaymentProvider.STRIPE).build();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(pm));
		when(repositoryPort.save(any(PaymentMethod.class))).thenAnswer(inv -> inv.getArgument(0));

		PaymentMethod result = paymentMethodService.update(id, true);

		assertThat(result.getIsDefault()).isTrue();
		verify(repositoryPort).save(pm);
	}

	@Test
	void delete_callsSoftDelete() {
		UUID id = UUID.randomUUID();

		paymentMethodService.delete(id);

		verify(repositoryPort).softDelete(id);
	}

}
