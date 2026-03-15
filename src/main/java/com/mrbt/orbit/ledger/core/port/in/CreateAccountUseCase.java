package com.mrbt.orbit.ledger.core.port.in;

import com.mrbt.orbit.common.core.port.in.UseCase;
import com.mrbt.orbit.ledger.core.model.Account;

public interface CreateAccountUseCase extends UseCase {
	Account createAccount(Account account);
}
