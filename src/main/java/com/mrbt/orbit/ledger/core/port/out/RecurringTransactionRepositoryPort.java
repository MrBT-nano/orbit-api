package com.mrbt.orbit.ledger.core.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;

public interface RecurringTransactionRepositoryPort {

	RecurringTransaction save(RecurringTransaction recurring);

	Optional<RecurringTransaction> findById(UUID id);

	PageResult<RecurringTransaction> findByUserId(UUID userId, int page, int size);

	List<RecurringTransaction> findActiveByNextOccurrenceBefore(LocalDate date);

}
