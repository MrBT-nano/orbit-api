package com.mrbt.orbit.payment.core.port.in;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.Subscription;

public interface CreateSubscriptionUseCase extends UseCase {

	Subscription create(Subscription subscription);

}
