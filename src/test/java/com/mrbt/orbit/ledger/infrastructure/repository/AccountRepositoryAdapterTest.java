package com.mrbt.orbit.ledger.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.common.exception.ResourceNotFoundException;
import com.mrbt.orbit.ledger.core.model.Account;
import com.mrbt.orbit.ledger.core.model.enums.AccountType;
import com.mrbt.orbit.ledger.infrastructure.entity.AccountEntity;
import com.mrbt.orbit.ledger.infrastructure.mapper.AccountMapper;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

	@Mock
	private AccountRepository springDataRepository;

	@Mock
	private AccountMapper mapper;

	@InjectMocks
	private AccountRepositoryAdapter adapter;

	@Test
	void save_convertsAndPersists() {
		Account domain = Account.builder().name("Test").type(AccountType.BANK).build();
		AccountEntity entity = new AccountEntity();
		AccountEntity savedEntity = new AccountEntity();
		Account expected = Account.builder().id(UUID.randomUUID()).name("Test").build();

		when(mapper.toEntity(domain)).thenReturn(entity);
		when(springDataRepository.save(entity)).thenReturn(savedEntity);
		when(mapper.toDomain(savedEntity)).thenReturn(expected);

		Account result = adapter.save(domain);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void findById_returnsAccount() {
		UUID id = UUID.randomUUID();
		AccountEntity entity = new AccountEntity();
		Account expected = Account.builder().id(id).name("Found").build();

		when(springDataRepository.findById(id)).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(expected);

		Optional<Account> result = adapter.findById(id);

		assertThat(result).isPresent().contains(expected);
	}

	@Test
	void findById_returnsEmptyWhenNotFound() {
		UUID id = UUID.randomUUID();
		when(springDataRepository.findById(id)).thenReturn(Optional.empty());

		assertThat(adapter.findById(id)).isEmpty();
	}

	@Test
	void findByUserId_returnsList() {
		UUID userId = UUID.randomUUID();
		AccountEntity entity = new AccountEntity();
		Account mapped = Account.builder().name("A1").build();

		when(springDataRepository.findByUserId(userId)).thenReturn(List.of(entity));
		when(mapper.toDomain(entity)).thenReturn(mapped);

		List<Account> result = adapter.findByUserId(userId);

		assertThat(result).hasSize(1).first().isEqualTo(mapped);
	}

	@Test
	void existsByUserIdAndName_delegatesToRepo() {
		UUID userId = UUID.randomUUID();
		when(springDataRepository.existsByUserIdAndName(userId, "Savings")).thenReturn(true);

		assertThat(adapter.existsByUserIdAndName(userId, "Savings")).isTrue();
	}

	@Test
	void updateBalance_delegatesToAtomicUpdate() {
		UUID accountId = UUID.randomUUID();
		BigDecimal amount = new BigDecimal("100.00");

		when(springDataRepository.updateBalanceAtomically(accountId, amount)).thenReturn(1);

		adapter.updateBalance(accountId, amount);
	}

	@Test
	void updateBalance_throwsWhenAccountNotFound() {
		UUID accountId = UUID.randomUUID();
		BigDecimal amount = new BigDecimal("100.00");

		when(springDataRepository.updateBalanceAtomically(accountId, amount)).thenReturn(0);

		assertThatThrownBy(() -> adapter.updateBalance(accountId, amount)).isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Account");
	}

}
