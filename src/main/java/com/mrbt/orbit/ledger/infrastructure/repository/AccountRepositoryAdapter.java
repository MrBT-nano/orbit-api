package com.mrbt.orbit.ledger.infrastructure.repository;

import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.port.out.AccountRepositoryPort;
import com.mrbt.orbit.ledger.infrastructure.entity.AccountEntity;
import com.mrbt.orbit.ledger.infrastructure.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

	private final AccountRepository springDataRepository;
	private final AccountMapper mapper;

	@Override
	public Account save(Account account) {
		AccountEntity entity = mapper.toEntity(account);
		AccountEntity savedEntity = springDataRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Account> findById(UUID id) {
		return springDataRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public List<Account> findByUserId(UUID userId) {
		return springDataRepository.findByUserId(userId).stream().map(mapper::toDomain).collect(Collectors.toList());
	}

	@Override
	public boolean existsByUserIdAndName(UUID userId, String name) {
		return springDataRepository.existsByUserIdAndName(userId, name);
	}

	@Override
	public void updateBalance(UUID accountId, BigDecimal amount) {
		int updated = springDataRepository.updateBalanceAtomically(accountId, amount);
		if (updated == 0) {
			throw new ResourceNotFoundException("Account", "ID", accountId);
		}
	}
}
