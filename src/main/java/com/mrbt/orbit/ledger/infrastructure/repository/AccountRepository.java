package com.mrbt.orbit.ledger.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mrbt.orbit.ledger.infrastructure.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

	List<AccountEntity> findByUserId(UUID userId);

	boolean existsByUserIdAndName(UUID userId, String name);

	@Modifying
	@Query("UPDATE AccountEntity a SET a.currentBalance = a.currentBalance + :amount WHERE a.id = :id")
	int updateBalanceAtomically(@Param("id") UUID id, @Param("amount") BigDecimal amount);

}
