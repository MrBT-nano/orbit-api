package com.mrbt.orbit.payment.core.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.Subscription;

public interface UpdateSubscriptionUseCase extends UseCase {

	Subscription update(UUID id, String name, BigDecimal amount, Integer reminderDaysBefore);

	Subscription togglePause(UUID id);

}
