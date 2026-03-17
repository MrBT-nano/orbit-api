package com.mrbt.orbit.ledger.core.service;

import com.mrbt.orbit.common.exception.BadRequestException;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.model.Transaction;
import com.mrbt.orbit.ledger.core.model.enums.TransactionStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.out.AccountRepositoryPort;
import com.mrbt.orbit.ledger.core.port.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

	private final TransactionRepositoryPort transactionRepositoryPort;
	private final AccountRepositoryPort accountRepositoryPort;

	@Override
	@Transactional
	public Transaction createTransaction(Transaction transaction) {
		// 1. Validate associated account exists
		Account account = accountRepositoryPort.findById(transaction.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account", "ID", transaction.getAccountId()));

		// 2. Set defaults
		if (transaction.getStatus() == null) {
			transaction.setStatus(TransactionStatus.COMPLETED);
		}
		if (transaction.getTransactionDate() == null) {
			transaction.setTransactionDate(Instant.now());
		}
		if (transaction.getIsReviewed() == null) {
			transaction.setIsReviewed(false);
		}
		if (transaction.getExchangeRate() == null) {
			transaction.setExchangeRate(BigDecimal.ONE);
		}

		// 3. Prevent 0 amount transactions (meaningless in ledger)
		if (transaction.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			throw new BadRequestException("Transaction amount cannot be exactly zero.");
		}

		// 4. If transaction is COMPLETED, update the account balance
		// Positive amount = Income (increases balance)
		// Negative amount = Expense (decreases balance)
		if (transaction.getStatus() == TransactionStatus.COMPLETED) {
			accountRepositoryPort.updateBalance(transaction.getAccountId(), transaction.getAmount());
		}

		// 5. Save the transaction log
		return transactionRepositoryPort.save(transaction);
	}
}
