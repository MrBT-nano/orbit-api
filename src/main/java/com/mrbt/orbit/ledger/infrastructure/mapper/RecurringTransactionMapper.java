package com.mrbt.orbit.ledger.infrastructure.mapper;

import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.ledger.api.response.RecurringTransactionResponse;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.infrastructure.entity.RecurringTransactionEntity;

@Component
public class RecurringTransactionMapper {

	public RecurringTransaction toDomain(RecurringTransactionEntity entity) {
		if (entity == null)
			return null;
		return RecurringTransaction.builder().id(entity.getId()).userId(entity.getUserId())
				.accountId(entity.getAccountId()).categoryId(entity.getCategoryId()).amount(entity.getAmount())
				.currencyCode(entity.getCurrencyCode()).description(entity.getDescription())
				.frequency(entity.getFrequency()).nextOccurrence(entity.getNextOccurrence())
				.autoConfirm(entity.getAutoConfirm()).status(entity.getStatus())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	public RecurringTransactionEntity toEntity(RecurringTransaction domain) {
		if (domain == null)
			return null;
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setAccountId(domain.getAccountId());
		entity.setCategoryId(domain.getCategoryId());
		entity.setAmount(domain.getAmount());
		entity.setCurrencyCode(domain.getCurrencyCode());
		entity.setDescription(domain.getDescription());
		entity.setFrequency(domain.getFrequency());
		entity.setNextOccurrence(domain.getNextOccurrence());
		entity.setAutoConfirm(domain.getAutoConfirm());
		entity.setStatus(domain.getStatus());
		return entity;
	}

	public List<RecurringTransaction> toDomainList(List<RecurringTransactionEntity> entities) {
		if (entities == null)
			return null;
		return entities.stream().map(this::toDomain).toList();
	}

	public RecurringTransactionResponse toResponse(RecurringTransaction domain) {
		if (domain == null)
			return null;
		return RecurringTransactionResponse.builder().id(domain.getId()).userId(domain.getUserId())
				.accountId(domain.getAccountId()).categoryId(domain.getCategoryId()).amount(domain.getAmount())
				.currencyCode(domain.getCurrencyCode()).description(domain.getDescription())
				.frequency(domain.getFrequency() != null ? domain.getFrequency().name() : null)
				.nextOccurrence(domain.getNextOccurrence()).autoConfirm(domain.getAutoConfirm())
				.status(domain.getStatus() != null ? domain.getStatus().name() : null).createdAt(domain.getCreatedAt())
				.updatedAt(domain.getUpdatedAt()).build();
	}

	public List<RecurringTransactionResponse> toResponseList(List<RecurringTransaction> domains) {
		if (domains == null)
			return null;
		return domains.stream().map(this::toResponse).toList();
	}

}
