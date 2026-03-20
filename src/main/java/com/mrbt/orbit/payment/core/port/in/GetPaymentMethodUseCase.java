package com.mrbt.orbit.payment.core.port.in;

import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.PaymentMethod;

public interface GetPaymentMethodUseCase extends UseCase {

	Optional<PaymentMethod> findById(UUID id);

	PageResult<PaymentMethod> findByUserId(UUID userId, int page, int size);

}
