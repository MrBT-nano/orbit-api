package com.mrbt.orbit.ledger.core.port.in;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;

public interface CreateRecurringTransactionUseCase extends UseCase {

	RecurringTransaction create(RecurringTransaction recurring);

}
