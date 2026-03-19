package com.mrbt.orbit.ledger.core.port.in;

import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;

public interface GetRecurringTransactionUseCase extends UseCase {

	Optional<RecurringTransaction> findById(UUID id);

	PageResult<RecurringTransaction> findByUserId(UUID userId, int page, int size);

}
