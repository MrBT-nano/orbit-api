package com.mrbt.orbit.ledger.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.infrastructure.entity.RecurringTransactionEntity;
import com.mrbt.orbit.ledger.infrastructure.mapper.RecurringTransactionMapper;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionRepositoryAdapterTest {

	@Mock
	private RecurringTransactionRepository springDataRepository;

	@Mock
	private RecurringTransactionMapper mapper;

	@InjectMocks
	private RecurringTransactionRepositoryAdapter adapter;

	@Test
	void save_convertsAndPersists() {
		RecurringTransaction domain = RecurringTransaction.builder().description("Rent").build();
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		RecurringTransactionEntity savedEntity = new RecurringTransactionEntity();
		RecurringTransaction expected = RecurringTransaction.builder().id(UUID.randomUUID()).description("Rent")
				.build();

		when(mapper.toEntity(domain)).thenReturn(entity);
		when(springDataRepository.save(entity)).thenReturn(savedEntity);
		when(mapper.toDomain(savedEntity)).thenReturn(expected);

		RecurringTransaction result = adapter.save(domain);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void findById_returnsTransaction() {
		UUID id = UUID.randomUUID();
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		RecurringTransaction expected = RecurringTransaction.builder().id(id).build();

		when(springDataRepository.findById(id)).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(expected);

		Optional<RecurringTransaction> result = adapter.findById(id);

		assertThat(result).isPresent().contains(expected);
	}

	@Test
	void findById_returnsEmptyWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(springDataRepository.findById(id)).thenReturn(Optional.empty());

		assertThat(adapter.findById(id)).isEmpty();
	}

	@Test
	void findByUserId_returnsPageResult() {
		UUID userId = UUID.randomUUID();
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		RecurringTransaction mapped = RecurringTransaction.builder().description("Rent").build();
		Page<RecurringTransactionEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);

		when(springDataRepository.findByUserIdOrderByCreatedAtDesc(eq(userId), any())).thenReturn(page);
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		PageResult<RecurringTransaction> result = adapter.findByUserId(userId, 0, 10);

		assertThat(result.content()).hasSize(1).first().isEqualTo(mapped);
		assertThat(result.totalElements()).isEqualTo(1);
	}

	@Test
	void findActiveByNextOccurrenceBefore_delegatesToRepo() {
		LocalDate date = LocalDate.of(2026, 3, 23);
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		RecurringTransaction mapped = RecurringTransaction.builder().description("Due").build();

		when(springDataRepository.findByStatusAndNextOccurrenceLessThanEqual(RecurringTransactionStatus.ACTIVE, date))
				.thenReturn(List.of(entity));
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		List<RecurringTransaction> result = adapter.findActiveByNextOccurrenceBefore(date);

		assertThat(result).hasSize(1).first().isEqualTo(mapped);
	}

}
