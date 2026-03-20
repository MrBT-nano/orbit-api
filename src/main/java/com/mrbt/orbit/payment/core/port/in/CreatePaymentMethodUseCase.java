package com.mrbt.orbit.payment.core.port.in;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.PaymentMethod;

public interface CreatePaymentMethodUseCase extends UseCase {

	PaymentMethod create(PaymentMethod paymentMethod);

}
