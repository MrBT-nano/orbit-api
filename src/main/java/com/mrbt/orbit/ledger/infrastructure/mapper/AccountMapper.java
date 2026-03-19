package com.mrbt.orbit.ledger.infrastructure.mapper;

import com.mrbt.orbit.ledger.api.request.CreateAccountRequest;
import com.mrbt.orbit.ledger.api.response.AccountResponse;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.infrastructure.entity.AccountEntity;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class AccountMapper {

	public Account toDomain(AccountEntity entity) {
		if (entity == null)
			return null;

		return Account.builder().id(entity.getId()).userId(entity.getUserId()).name(entity.getName())
				.type(entity.getType()).currencyCode(entity.getCurrencyCode())
				.currentBalance(entity.getCurrentBalance()).plaidAccountId(entity.getPlaidAccountId())
				.status(entity.getStatus())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	public AccountEntity toEntity(Account domain) {
		if (domain == null)
			return null;

		AccountEntity entity = new AccountEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setName(domain.getName());
		entity.setType(domain.getType());
		entity.setCurrencyCode(domain.getCurrencyCode());
		entity.setCurrentBalance(domain.getCurrentBalance());
		entity.setPlaidAccountId(domain.getPlaidAccountId());
		entity.setStatus(domain.getStatus());

		return entity;
	}

	public Account toDomain(CreateAccountRequest request) {
		if (request == null)
			return null;

		return Account.builder().userId(request.userId()).name(request.name()).type(request.type())
				.currencyCode(request.currencyCode()).currentBalance(request.initialBalance())
				.plaidAccountId(request.plaidAccountId()).build();
	}

	public AccountResponse toResponse(Account domain) {
		if (domain == null)
			return null;

		return AccountResponse.builder().id(domain.getId()).userId(domain.getUserId()).name(domain.getName())
				.type(domain.getType() != null ? domain.getType().name() : null).currencyCode(domain.getCurrencyCode())
				.currentBalance(domain.getCurrentBalance()).plaidAccountId(domain.getPlaidAccountId())
				.status(domain.getStatus() != null ? domain.getStatus().name() : null).createdAt(domain.getCreatedAt())
				.updatedAt(domain.getUpdatedAt()).build();
	}

	public List<AccountResponse> toResponseList(List<Account> domains) {
		if (domains == null)
			return null;
		return domains.stream().map(this::toResponse).toList();
	}
}
