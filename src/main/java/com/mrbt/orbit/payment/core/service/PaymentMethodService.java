package com.mrbt.orbit.payment.core.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.port.in.CreatePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.DeletePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.GetPaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdatePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.out.PaymentMethodRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService
		implements
			CreatePaymentMethodUseCase,
			GetPaymentMethodUseCase,
			UpdatePaymentMethodUseCase,
			DeletePaymentMethodUseCase {

	private final PaymentMethodRepositoryPort repositoryPort;

	@Override
	@Transactional
	public PaymentMethod create(PaymentMethod paymentMethod) {
		if (paymentMethod.getStatus() == null) {
			paymentMethod.setStatus(PaymentMethodStatus.ACTIVE);
		}
		if (paymentMethod.getIsDefault() == null) {
			paymentMethod.setIsDefault(false);
		}
		return repositoryPort.save(paymentMethod);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PaymentMethod> findById(UUID id) {
		return repositoryPort.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResult<PaymentMethod> findByUserId(UUID userId, int page, int size) {
		return repositoryPort.findByUserId(userId, page, size);
	}

	@Override
	@Transactional
	public PaymentMethod update(UUID id, Boolean isDefault) {
		PaymentMethod paymentMethod = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "ID", id));
		if (isDefault != null) {
			paymentMethod.setIsDefault(isDefault);
		}
		return repositoryPort.save(paymentMethod);
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		repositoryPort.softDelete(id);
	}

}
