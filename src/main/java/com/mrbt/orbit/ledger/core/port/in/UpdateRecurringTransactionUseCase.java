package com.mrbt.orbit.ledger.core.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;

public interface UpdateRecurringTransactionUseCase extends UseCase {

	RecurringTransaction update(UUID id, String description, BigDecimal amount, UUID categoryId);

	RecurringTransaction togglePause(UUID id);

}
