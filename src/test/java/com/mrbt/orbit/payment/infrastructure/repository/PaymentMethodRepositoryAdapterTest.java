package com.mrbt.orbit.payment.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;
import com.mrbt.orbit.payment.infrastructure.entity.PaymentMethodEntity;
import com.mrbt.orbit.payment.infrastructure.mapper.PaymentMethodEntityMapper;

@ExtendWith(MockitoExtension.class)
class PaymentMethodRepositoryAdapterTest {

	@Mock
	private PaymentMethodRepository paymentMethodRepository;

	@Mock
	private PaymentMethodEntityMapper mapper;

	@InjectMocks
	private PaymentMethodRepositoryAdapter adapter;

	@Test
	void save_convertsAndPersists() {
		PaymentMethod domain = PaymentMethod.builder().provider(PaymentProvider.STRIPE).build();
		PaymentMethodEntity entity = new PaymentMethodEntity();
		PaymentMethodEntity savedEntity = new PaymentMethodEntity();
		PaymentMethod expected = PaymentMethod.builder().id(UUID.randomUUID()).provider(PaymentProvider.STRIPE).build();

		when(mapper.toEntity(domain)).thenReturn(entity);
		when(paymentMethodRepository.save(entity)).thenReturn(savedEntity);
		when(mapper.toDomain(savedEntity)).thenReturn(expected);

		PaymentMethod result = adapter.save(domain);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void findById_returnsPaymentMethod() {
		UUID id = UUID.randomUUID();
		PaymentMethodEntity entity = new PaymentMethodEntity();
		PaymentMethod expected = PaymentMethod.builder().id(id).build();

		when(paymentMethodRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(expected);

		Optional<PaymentMethod> result = adapter.findById(id);

		assertThat(result).isPresent().contains(expected);
	}

	@Test
	void findById_returnsEmptyWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(paymentMethodRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

		assertThat(adapter.findById(id)).isEmpty();
	}

	@Test
	void findByUserId_returnsPageResult() {
		UUID userId = UUID.randomUUID();
		PaymentMethodEntity entity = new PaymentMethodEntity();
		PaymentMethod mapped = PaymentMethod.builder().provider(PaymentProvider.STRIPE).build();
		Page<PaymentMethodEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);

		when(paymentMethodRepository.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(eq(userId), any()))
				.thenReturn(page);
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		PageResult<PaymentMethod> result = adapter.findByUserId(userId, 0, 10);

		assertThat(result.content()).hasSize(1).first().isEqualTo(mapped);
		assertThat(result.totalElements()).isEqualTo(1);
	}

	@Test
	void softDelete_deletesWhenFound() {
		UUID id = UUID.randomUUID();
		PaymentMethodEntity entity = new PaymentMethodEntity();

		when(paymentMethodRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(entity));

		adapter.softDelete(id);

		verify(paymentMethodRepository).save(entity);
	}

	@Test
	void softDelete_doesNothingWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(paymentMethodRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.empty());

		adapter.softDelete(id);
	}

}
