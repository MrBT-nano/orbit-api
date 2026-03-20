package com.mrbt.orbit.payment.core.port.in;

import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;

public interface DeleteSubscriptionUseCase extends UseCase {

	void delete(UUID id);

}
