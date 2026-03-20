package com.mrbt.orbit.payment.core.port.in;

import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.PaymentMethod;

public interface UpdatePaymentMethodUseCase extends UseCase {

	PaymentMethod update(UUID id, Boolean isDefault);

}
