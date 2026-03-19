package com.mrbt.orbit.ledger.core.port.in;

import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;

public interface DeleteRecurringTransactionUseCase extends UseCase {

	void cancel(UUID id);

}
