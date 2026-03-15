package com.mrbt.orbit.ledger.core.service;

import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.port.in.GetAccountUseCase;
import com.mrbt.orbit.ledger.core.port.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAccountService implements GetAccountUseCase {

	private final AccountRepositoryPort accountRepositoryPort;

	@Override
	@Transactional(readOnly = true)
	public Optional<Account> getAccountById(UUID accountId) {
		return accountRepositoryPort.findById(accountId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> getAccountsByUserId(UUID userId) {
		return accountRepositoryPort.findByUserId(userId);
	}
}
