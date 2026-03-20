package com.mrbt.orbit.payment.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.port.out.PaymentMethodRepositoryPort;
import com.mrbt.orbit.payment.infrastructure.entity.PaymentMethodEntity;
import com.mrbt.orbit.payment.infrastructure.mapper.PaymentMethodEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepositoryPort {

	private final PaymentMethodRepository paymentMethodRepository;

	private final PaymentMethodEntityMapper mapper;

	@Override
	public PaymentMethod save(PaymentMethod paymentMethod) {
		PaymentMethodEntity entity = mapper.toEntity(paymentMethod);
		PaymentMethodEntity saved = paymentMethodRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<PaymentMethod> findById(UUID id) {
		return paymentMethodRepository.findByIdAndDeletedAtIsNull(id).map(mapper::toDomain);
	}

	@Override
	public PageResult<PaymentMethod> findByUserId(UUID userId, int page, int size) {
		Page<PaymentMethodEntity> entityPage = paymentMethodRepository
				.findByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
		return new PageResult<>(mapper.toDomainList(entityPage.getContent()), entityPage.getTotalElements(),
				entityPage.getTotalPages(), entityPage.getNumber(), entityPage.getSize());
	}

	@Override
	public void softDelete(UUID id) {
		paymentMethodRepository.findByIdAndDeletedAtIsNull(id).ifPresent(entity -> {
			entity.softDelete();
			paymentMethodRepository.save(entity);
		});
	}

}
