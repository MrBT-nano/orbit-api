package com.mrbt.orbit.ledger.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.core.port.out.RecurringTransactionRepositoryPort;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionServiceTest {

	@Mock
	private RecurringTransactionRepositoryPort repositoryPort;

	@InjectMocks
	private RecurringTransactionService service;

	private RecurringTransaction buildRecurring() {
		return RecurringTransaction.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).categoryId(UUID.randomUUID()).amount(new BigDecimal("100.00"))
				.currencyCode("USD").description("Monthly rent").frequency(Frequency.MONTHLY)
				.nextOccurrence(LocalDate.now()).build();
	}

	@Test
	void create_defaultsStatusToActive() {
		RecurringTransaction recurring = buildRecurring();
		recurring.setStatus(null);

		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		RecurringTransaction result = service.create(recurring);

		assertThat(result.getStatus()).isEqualTo(RecurringTransactionStatus.ACTIVE);
	}

	@Test
	void create_defaultsAutoConfirmToTrue() {
		RecurringTransaction recurring = buildRecurring();
		recurring.setAutoConfirm(null);

		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		RecurringTransaction result = service.create(recurring);

		assertThat(result.getAutoConfirm()).isTrue();
	}

	@Test
	void findById_returnsRecurring() {
		RecurringTransaction recurring = buildRecurring();
		UUID id = recurring.getId();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(recurring));

		Optional<RecurringTransaction> result = service.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(id);
	}

	@Test
	void findByUserId_returnsPageResult() {
		UUID userId = UUID.randomUUID();
		RecurringTransaction recurring = buildRecurring();
		PageResult<RecurringTransaction> page = new PageResult<>(List.of(recurring), 1L, 1, 0, 20);

		when(repositoryPort.findByUserId(userId, 0, 20)).thenReturn(page);

		PageResult<RecurringTransaction> result = service.findByUserId(userId, 0, 20);

		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1L);
	}

	@Test
	void update_updatesDescription() {
		RecurringTransaction recurring = buildRecurring();
		UUID id = recurring.getId();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(recurring));
		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		RecurringTransaction result = service.update(id, "Updated description", null, null);

		assertThat(result.getDescription()).isEqualTo("Updated description");
	}

	@Test
	void update_throwsWhenNotFound() {
		UUID id = UUID.randomUUID();

		when(repositoryPort.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.update(id, "desc", null, null)).isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("RecurringTransaction");
	}

	@Test
	void togglePause_activeToPaused() {
		RecurringTransaction recurring = buildRecurring();
		recurring.setStatus(RecurringTransactionStatus.ACTIVE);
		UUID id = recurring.getId();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(recurring));
		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		RecurringTransaction result = service.togglePause(id);

		assertThat(result.getStatus()).isEqualTo(RecurringTransactionStatus.PAUSED);
	}

	@Test
	void togglePause_pausedToActive() {
		RecurringTransaction recurring = buildRecurring();
		recurring.setStatus(RecurringTransactionStatus.PAUSED);
		UUID id = recurring.getId();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(recurring));
		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		RecurringTransaction result = service.togglePause(id);

		assertThat(result.getStatus()).isEqualTo(RecurringTransactionStatus.ACTIVE);
	}

	@Test
	void cancel_setsStatusToCancelled() {
		RecurringTransaction recurring = buildRecurring();
		UUID id = recurring.getId();

		when(repositoryPort.findById(id)).thenReturn(Optional.of(recurring));
		when(repositoryPort.save(any(RecurringTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

		service.cancel(id);

		assertThat(recurring.getStatus()).isEqualTo(RecurringTransactionStatus.CANCELLED);
		verify(repositoryPort).save(recurring);
	}

	@Test
	void cancel_throwsWhenNotFound() {
		UUID id = UUID.randomUUID();

		when(repositoryPort.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.cancel(id)).isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("RecurringTransaction");
	}

}
