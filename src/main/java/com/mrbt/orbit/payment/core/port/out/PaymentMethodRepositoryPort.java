package com.mrbt.orbit.payment.core.port.out;

import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.core.model.PaymentMethod;

public interface PaymentMethodRepositoryPort {

	PaymentMethod save(PaymentMethod paymentMethod);

	Optional<PaymentMethod> findById(UUID id);

	PageResult<PaymentMethod> findByUserId(UUID userId, int page, int size);

	void softDelete(UUID id);

}
