package com.mrbt.orbit.ledger.core.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.DeleteRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.GetRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.UpdateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.out.RecurringTransactionRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService
		implements
			CreateRecurringTransactionUseCase,
			GetRecurringTransactionUseCase,
			UpdateRecurringTransactionUseCase,
			DeleteRecurringTransactionUseCase {

	private final RecurringTransactionRepositoryPort repositoryPort;

	@Override
	@Transactional
	public RecurringTransaction create(RecurringTransaction recurring) {
		if (recurring.getStatus() == null) {
			recurring.setStatus(RecurringTransactionStatus.ACTIVE);
		}
		if (recurring.getAutoConfirm() == null) {
			recurring.setAutoConfirm(true);
		}
		return repositoryPort.save(recurring);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<RecurringTransaction> findById(UUID id) {
		return repositoryPort.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResult<RecurringTransaction> findByUserId(UUID userId, int page, int size) {
		return repositoryPort.findByUserId(userId, page, size);
	}

	@Override
	@Transactional
	public RecurringTransaction update(UUID id, String description, BigDecimal amount, UUID categoryId) {
		RecurringTransaction recurring = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("RecurringTransaction", "ID", id));
		if (description != null)
			recurring.setDescription(description);
		if (amount != null)
			recurring.setAmount(amount);
		if (categoryId != null)
			recurring.setCategoryId(categoryId);
		return repositoryPort.save(recurring);
	}

	@Override
	@Transactional
	public RecurringTransaction togglePause(UUID id) {
		RecurringTransaction recurring = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("RecurringTransaction", "ID", id));
		if (recurring.getStatus() == RecurringTransactionStatus.ACTIVE) {
			recurring.setStatus(RecurringTransactionStatus.PAUSED);
		} else if (recurring.getStatus() == RecurringTransactionStatus.PAUSED) {
			recurring.setStatus(RecurringTransactionStatus.ACTIVE);
		}
		return repositoryPort.save(recurring);
	}

	@Override
	@Transactional
	public void cancel(UUID id) {
		RecurringTransaction recurring = repositoryPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("RecurringTransaction", "ID", id));
		recurring.setStatus(RecurringTransactionStatus.CANCELLED);
		repositoryPort.save(recurring);
	}

}
