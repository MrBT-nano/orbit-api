package com.mrbt.orbit.ledger.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.ledger.api.response.RecurringTransactionResponse;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.infrastructure.entity.RecurringTransactionEntity;

class RecurringTransactionMapperTest {

	private final RecurringTransactionMapper mapper = new RecurringTransactionMapper();

	@Test
	void toDomain_mapsAllFields() {
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();
		UUID categoryId = UUID.randomUUID();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setAccountId(accountId);
		entity.setCategoryId(categoryId);
		entity.setAmount(new BigDecimal("500.00"));
		entity.setCurrencyCode("THB");
		entity.setDescription("Monthly rent");
		entity.setFrequency(Frequency.MONTHLY);
		entity.setNextOccurrence(LocalDate.of(2026, 4, 1));
		entity.setAutoConfirm(true);
		entity.setStatus(RecurringTransactionStatus.ACTIVE);
		entity.setCreatedAt(Instant.parse("2026-01-01T00:00:00Z"));
		entity.setUpdatedAt(Instant.parse("2026-01-02T00:00:00Z"));

		RecurringTransaction result = mapper.toDomain(entity);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccountId()).isEqualTo(accountId);
		assertThat(result.getCategoryId()).isEqualTo(categoryId);
		assertThat(result.getAmount()).isEqualByComparingTo("500.00");
		assertThat(result.getCurrencyCode()).isEqualTo("THB");
		assertThat(result.getDescription()).isEqualTo("Monthly rent");
		assertThat(result.getFrequency()).isEqualTo(Frequency.MONTHLY);
		assertThat(result.getNextOccurrence()).isEqualTo(LocalDate.of(2026, 4, 1));
		assertThat(result.getAutoConfirm()).isTrue();
		assertThat(result.getStatus()).isEqualTo(RecurringTransactionStatus.ACTIVE);
		assertThat(result.getCreatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
		assertThat(result.getUpdatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain((RecurringTransactionEntity) null)).isNull();
	}

	@Test
	void toDomain_handlesNullTimestamps() {
		RecurringTransactionEntity entity = new RecurringTransactionEntity();
		entity.setFrequency(Frequency.WEEKLY);
		entity.setStatus(RecurringTransactionStatus.ACTIVE);

		RecurringTransaction result = mapper.toDomain(entity);

		assertThat(result.getCreatedAt()).isNull();
		assertThat(result.getUpdatedAt()).isNull();
	}

	@Test
	void toEntity_mapsAllFields() {
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();
		RecurringTransaction domain = RecurringTransaction.builder().id(id).userId(userId).accountId(accountId)
				.amount(new BigDecimal("1000.00")).currencyCode("USD").description("Salary")
				.frequency(Frequency.MONTHLY).nextOccurrence(LocalDate.of(2026, 5, 1)).autoConfirm(false)
				.status(RecurringTransactionStatus.PAUSED).build();

		RecurringTransactionEntity result = mapper.toEntity(domain);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getAccountId()).isEqualTo(accountId);
		assertThat(result.getAmount()).isEqualByComparingTo("1000.00");
		assertThat(result.getCurrencyCode()).isEqualTo("USD");
		assertThat(result.getDescription()).isEqualTo("Salary");
		assertThat(result.getFrequency()).isEqualTo(Frequency.MONTHLY);
		assertThat(result.getNextOccurrence()).isEqualTo(LocalDate.of(2026, 5, 1));
		assertThat(result.getAutoConfirm()).isFalse();
		assertThat(result.getStatus()).isEqualTo(RecurringTransactionStatus.PAUSED);
	}

	@Test
	void toEntity_returnsNullForNull() {
		assertThat(mapper.toEntity(null)).isNull();
	}

	@Test
	void toDomainList_convertsList() {
		RecurringTransactionEntity e1 = new RecurringTransactionEntity();
		e1.setDescription("Rent");
		e1.setFrequency(Frequency.MONTHLY);
		e1.setStatus(RecurringTransactionStatus.ACTIVE);
		RecurringTransactionEntity e2 = new RecurringTransactionEntity();
		e2.setDescription("Gym");
		e2.setFrequency(Frequency.MONTHLY);
		e2.setStatus(RecurringTransactionStatus.ACTIVE);

		List<RecurringTransaction> result = mapper.toDomainList(List.of(e1, e2));

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getDescription()).isEqualTo("Rent");
		assertThat(result.get(1).getDescription()).isEqualTo("Gym");
	}

	@Test
	void toDomainList_returnsNullForNull() {
		assertThat(mapper.toDomainList(null)).isNull();
	}

	@Test
	void toResponse_mapsAllFields() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		RecurringTransaction domain = RecurringTransaction.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).categoryId(UUID.randomUUID()).amount(new BigDecimal("250.00"))
				.currencyCode("EUR").description("Netflix").frequency(Frequency.MONTHLY)
				.nextOccurrence(LocalDate.of(2026, 4, 15)).autoConfirm(true).status(RecurringTransactionStatus.ACTIVE)
				.createdAt(now).updatedAt(now).build();

		RecurringTransactionResponse result = mapper.toResponse(domain);

		assertThat(result.id()).isEqualTo(domain.getId());
		assertThat(result.frequency()).isEqualTo("MONTHLY");
		assertThat(result.status()).isEqualTo("ACTIVE");
		assertThat(result.amount()).isEqualByComparingTo("250.00");
		assertThat(result.description()).isEqualTo("Netflix");
	}

	@Test
	void toResponse_returnsNullForNull() {
		assertThat(mapper.toResponse(null)).isNull();
	}

	@Test
	void toResponse_handlesNullEnums() {
		RecurringTransaction domain = RecurringTransaction.builder().description("Minimal").build();

		RecurringTransactionResponse result = mapper.toResponse(domain);

		assertThat(result.frequency()).isNull();
		assertThat(result.status()).isNull();
	}

	@Test
	void toResponseList_convertsList() {
		List<RecurringTransaction> domains = List.of(
				RecurringTransaction.builder().description("A").frequency(Frequency.WEEKLY)
						.status(RecurringTransactionStatus.ACTIVE).build(),
				RecurringTransaction.builder().description("B").frequency(Frequency.MONTHLY)
						.status(RecurringTransactionStatus.PAUSED).build());

		List<RecurringTransactionResponse> result = mapper.toResponseList(domains);

		assertThat(result).hasSize(2);
		assertThat(result.get(0).description()).isEqualTo("A");
	}

	@Test
	void toResponseList_returnsNullForNull() {
		assertThat(mapper.toResponseList(null)).isNull();
	}

}
