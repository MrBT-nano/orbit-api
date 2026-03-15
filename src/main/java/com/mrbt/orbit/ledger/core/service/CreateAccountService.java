package com.mrbt.orbit.ledger.core.service;

import com.mrbt.orbit.common.exception.DuplicateResourceException;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.model.enums.AccountStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateAccountUseCase;
import com.mrbt.orbit.ledger.core.port.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {

	private final AccountRepositoryPort accountRepositoryPort;

	@Override
	@Transactional
	public Account createAccount(Account account) {
		if (accountRepositoryPort.existsByUserIdAndName(account.getUserId(), account.getName())) {
			throw new DuplicateResourceException("Account", "Name", account.getName());
		}

		if (account.getStatus() == null) {
			account.setStatus(AccountStatus.ACTIVE);
		}

		if (account.getCurrentBalance() == null) {
			account.setCurrentBalance(BigDecimal.ZERO);
		}

		return accountRepositoryPort.save(account);
	}
}
