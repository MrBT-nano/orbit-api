package com.mrbt.orbit.payment.core.port.in;

import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.payment.core.model.Subscription;

public interface GetSubscriptionUseCase extends UseCase {

	Optional<Subscription> findById(UUID id);

	PageResult<Subscription> findByUserId(UUID userId, int page, int size);

}
