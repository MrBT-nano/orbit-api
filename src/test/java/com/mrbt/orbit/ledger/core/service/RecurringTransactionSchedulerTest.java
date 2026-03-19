package com.mrbt.orbit.ledger.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.Transaction;
import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.core.model.enums.TransactionStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.out.RecurringTransactionRepositoryPort;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionSchedulerTest {

	@Mock
	private RecurringTransactionRepositoryPort recurringRepo;

	@Mock
	private CreateTransactionUseCase createTransactionUseCase;

	@InjectMocks
	private RecurringTransactionScheduler scheduler;

	private RecurringTransaction buildActiveRule(LocalDate nextOccurrence, boolean autoConfirm) {
		return RecurringTransaction.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).categoryId(UUID.randomUUID()).amount(new BigDecimal("500.00"))
				.currencyCode("USD").description("Monthly rent").frequency(Frequency.MONTHLY)
				.nextOccurrence(nextOccurrence).autoConfirm(autoConfirm).status(RecurringTransactionStatus.ACTIVE)
				.build();
	}

	@Test
	void processRecurringTransactions_createsTransactionAndAdvancesDate() {
		LocalDate today = LocalDate.now();
		RecurringTransaction rule = buildActiveRule(today, true);

		when(recurringRepo.findActiveByNextOccurrenceBefore(today)).thenReturn(List.of(rule));
		when(createTransactionUseCase.createTransaction(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		scheduler.processRecurringTransactions();

		ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
		verify(createTransactionUseCase).createTransaction(txCaptor.capture());
		Transaction createdTx = txCaptor.getValue();
		assertThat(createdTx.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
		assertThat(createdTx.getAccountId()).isEqualTo(rule.getAccountId());
		assertThat(createdTx.getAmount()).isEqualByComparingTo(rule.getAmount());

		assertThat(rule.getNextOccurrence()).isEqualTo(today.plusMonths(1));
		verify(recurringRepo).save(rule);
	}

	@Test
	void processRecurringTransactions_pendingWhenAutoConfirmFalse() {
		LocalDate today = LocalDate.now();
		RecurringTransaction rule = buildActiveRule(today, false);

		when(recurringRepo.findActiveByNextOccurrenceBefore(today)).thenReturn(List.of(rule));
		when(createTransactionUseCase.createTransaction(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

		scheduler.processRecurringTransactions();

		ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
		verify(createTransactionUseCase).createTransaction(txCaptor.capture());
		assertThat(txCaptor.getValue().getStatus()).isEqualTo(TransactionStatus.PENDING);
	}

	@Test
	void processRecurringTransactions_doesNothingWhenNoDueRules() {
		LocalDate today = LocalDate.now();

		when(recurringRepo.findActiveByNextOccurrenceBefore(today)).thenReturn(Collections.emptyList());

		scheduler.processRecurringTransactions();

		verify(createTransactionUseCase, never()).createTransaction(any());
	}

}
