package com.mrbt.orbit.ledger.core.port.out;

import com.mrbt.orbit.ledger.core.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
	Account save(Account account);
	Optional<Account> findById(UUID id);
	List<Account> findByUserId(UUID userId);
	boolean existsByUserIdAndName(UUID userId, String name);
	void updateBalance(UUID accountId, BigDecimal amount);
}
