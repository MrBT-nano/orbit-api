package com.mrbt.orbit.payment.infrastructure.mapper;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.common.infrastructure.mapper.AbstractNullSafeMapper;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.infrastructure.entity.PaymentMethodEntity;

@Component
public class PaymentMethodEntityMapper extends AbstractNullSafeMapper<PaymentMethodEntity, PaymentMethod> {

	@Override
	public PaymentMethod toDomain(PaymentMethodEntity entity) {
		if (entity == null) {
			return null;
		}

		return PaymentMethod.builder().id(entity.getId()).userId(entity.getUserId()).provider(entity.getProvider())
				.providerReferenceId(entity.getProviderReferenceId()).lastFourDigits(entity.getLastFourDigits())
				.isDefault(entity.getIsDefault()).status(entity.getStatus())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	@Override
	public PaymentMethodEntity toEntity(PaymentMethod domain) {
		if (domain == null) {
			return null;
		}

		PaymentMethodEntity entity = new PaymentMethodEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setProvider(domain.getProvider());
		entity.setProviderReferenceId(domain.getProviderReferenceId());
		entity.setLastFourDigits(domain.getLastFourDigits());
		entity.setIsDefault(domain.getIsDefault());
		entity.setStatus(domain.getStatus());
		return entity;
	}

}
