package com.mrbt.orbit.ledger.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.infrastructure.entity.RecurringTransactionEntity;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransactionEntity, UUID> {

	Page<RecurringTransactionEntity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

	List<RecurringTransactionEntity> findByStatusAndNextOccurrenceLessThanEqual(RecurringTransactionStatus status,
			LocalDate date);

}
