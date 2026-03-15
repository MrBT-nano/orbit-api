package com.mrbt.orbit.ledger.core.port.in;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.ledger.core.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetAccountUseCase extends UseCase {
	Optional<Account> getAccountById(UUID accountId);
	List<Account> getAccountsByUserId(UUID userId);
}
