package com.mrbt.orbit.ledger.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.common.exception.BadRequestException;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.model.Transaction;
import com.mrbt.orbit.ledger.core.model.enums.AccountStatus;
import com.mrbt.orbit.ledger.core.model.enums.TransactionStatus;
import com.mrbt.orbit.ledger.core.port.out.AccountRepositoryPort;
import com.mrbt.orbit.ledger.core.port.out.TransactionRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreateTransactionServiceTest {

	@Mock
	private TransactionRepositoryPort transactionRepositoryPort;

	@Mock
	private AccountRepositoryPort accountRepositoryPort;

	@InjectMocks
	private CreateTransactionService createTransactionService;

	private Account buildActiveAccount(BigDecimal balance) {
		return Account.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).name("Main")
				.status(AccountStatus.ACTIVE).currentBalance(balance).currencyCode("USD").build();
	}

	@Test
	void createTransaction_setsDefaults() {
		Account account = buildActiveAccount(new BigDecimal("500.00"));
		UUID accountId = account.getId();

		Transaction tx = Transaction.builder().accountId(accountId).amount(new BigDecimal("100.00")).currencyCode("USD")
				.build();

		when(accountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));
		when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		Transaction result = createTransactionService.createTransaction(tx);

		assertThat(result.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
		assertThat(result.getTransactionDate()).isNotNull();
		assertThat(result.getIsReviewed()).isFalse();
		assertThat(result.getExchangeRate()).isEqualByComparingTo(BigDecimal.ONE);
	}

	@Test
	void createTransaction_updatesBalanceForCompletedIncome() {
		Account account = buildActiveAccount(new BigDecimal("500.00"));
		UUID accountId = account.getId();
		BigDecimal amount = new BigDecimal("200.00");

		Transaction tx = Transaction.builder().accountId(accountId).amount(amount).currencyCode("USD").build();

		when(accountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));
		when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		createTransactionService.createTransaction(tx);

		verify(accountRepositoryPort).updateBalance(eq(accountId), eq(amount));
	}

	@Test
	void createTransaction_updatesBalanceForCompletedExpense() {
		Account account = buildActiveAccount(new BigDecimal("500.00"));
		UUID accountId = account.getId();
		BigDecimal amount = new BigDecimal("-150.00");

		Transaction tx = Transaction.builder().accountId(accountId).amount(amount).currencyCode("USD").build();

		when(accountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));
		when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		createTransactionService.createTransaction(tx);

		verify(accountRepositoryPort).updateBalance(eq(accountId), eq(amount));
	}

	@Test
	void createTransaction_doesNotUpdateBalanceForPendingTransaction() {
		Account account = buildActiveAccount(new BigDecimal("500.00"));
		UUID accountId = account.getId();

		Transaction tx = Transaction.builder().accountId(accountId).amount(new BigDecimal("100.00")).currencyCode("USD")
				.status(TransactionStatus.PENDING).transactionDate(Instant.now()).isReviewed(false)
				.exchangeRate(BigDecimal.ONE).build();

		when(accountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));
		when(transactionRepositoryPort.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		createTransactionService.createTransaction(tx);

		verify(accountRepositoryPort, never()).updateBalance(any(), any());
	}

	@Test
	void createTransaction_throwsOnAccountNotFound() {
		UUID fakeAccountId = UUID.randomUUID();
		Transaction tx = Transaction.builder().accountId(fakeAccountId).amount(new BigDecimal("50.00"))
				.currencyCode("USD").build();

		when(accountRepositoryPort.findById(fakeAccountId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> createTransactionService.createTransaction(tx))
				.isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("Account");

		verify(transactionRepositoryPort, never()).save(any());
	}

	@Test
	void createTransaction_throwsOnZeroAmount() {
		Account account = buildActiveAccount(new BigDecimal("500.00"));
		UUID accountId = account.getId();

		Transaction tx = Transaction.builder().accountId(accountId).amount(BigDecimal.ZERO).currencyCode("USD").build();

		when(accountRepositoryPort.findById(accountId)).thenReturn(Optional.of(account));

		assertThatThrownBy(() -> createTransactionService.createTransaction(tx)).isInstanceOf(BadRequestException.class)
				.hasMessageContaining("zero");

		verify(transactionRepositoryPort, never()).save(any());
	}

}
