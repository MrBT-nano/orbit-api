package com.mrbt.orbit.ledger.core.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.Transaction;
import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import com.mrbt.orbit.ledger.core.model.enums.TransactionStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.out.RecurringTransactionRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecurringTransactionScheduler {

	private final RecurringTransactionRepositoryPort recurringRepo;

	private final CreateTransactionUseCase createTransactionUseCase;

	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void processRecurringTransactions() {
		LocalDate today = LocalDate.now();
		List<RecurringTransaction> dueRules = recurringRepo.findActiveByNextOccurrenceBefore(today);

		log.info("Processing {} recurring transactions due on or before {}", dueRules.size(), today);

		for (RecurringTransaction rule : dueRules) {
			Transaction tx = Transaction.builder().accountId(rule.getAccountId()).categoryId(rule.getCategoryId())
					.amount(rule.getAmount()).currencyCode(rule.getCurrencyCode()).description(rule.getDescription())
					.status(Boolean.TRUE.equals(rule.getAutoConfirm())
							? TransactionStatus.COMPLETED
							: TransactionStatus.PENDING)
					.recurringTransactionId(rule.getId()).exchangeRate(BigDecimal.ONE).build();

			createTransactionUseCase.createTransaction(tx);

			rule.setNextOccurrence(advanceDate(rule.getNextOccurrence(), rule.getFrequency()));
			recurringRepo.save(rule);

			log.info("Created transaction for recurring rule {} next occurrence: {}", rule.getId(),
					rule.getNextOccurrence());
		}
	}

	private LocalDate advanceDate(LocalDate current, Frequency frequency) {
		return switch (frequency) {
			case DAILY -> current.plusDays(1);
			case WEEKLY -> current.plusWeeks(1);
			case BIWEEKLY -> current.plusWeeks(2);
			case MONTHLY -> current.plusMonths(1);
			case QUARTERLY -> current.plusMonths(3);
			case YEARLY -> current.plusYears(1);
		};
	}

}
