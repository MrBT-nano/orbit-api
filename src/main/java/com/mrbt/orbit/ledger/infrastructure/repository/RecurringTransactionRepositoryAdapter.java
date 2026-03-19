package com.mrbt.orbit.ledger.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.core.port.out.RecurringTransactionRepositoryPort;
import com.mrbt.orbit.ledger.infrastructure.entity.RecurringTransactionEntity;
import com.mrbt.orbit.ledger.infrastructure.mapper.RecurringTransactionMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecurringTransactionRepositoryAdapter implements RecurringTransactionRepositoryPort {

	private final RecurringTransactionRepository springDataRepository;

	private final RecurringTransactionMapper mapper;

	@Override
	public RecurringTransaction save(RecurringTransaction recurring) {
		RecurringTransactionEntity entity = mapper.toEntity(recurring);
		RecurringTransactionEntity saved = springDataRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<RecurringTransaction> findById(UUID id) {
		return springDataRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public PageResult<RecurringTransaction> findByUserId(UUID userId, int page, int size) {
		Page<RecurringTransactionEntity> entityPage = springDataRepository.findByUserIdOrderByCreatedAtDesc(userId,
				PageRequest.of(page, size));
		return new PageResult<>(mapper.toDomainList(entityPage.getContent()), entityPage.getTotalElements(),
				entityPage.getTotalPages(), entityPage.getNumber(), entityPage.getSize());
	}

	@Override
	public List<RecurringTransaction> findActiveByNextOccurrenceBefore(LocalDate date) {
		return mapper.toDomainList(springDataRepository
				.findByStatusAndNextOccurrenceLessThanEqual(RecurringTransactionStatus.ACTIVE, date));
	}

}
