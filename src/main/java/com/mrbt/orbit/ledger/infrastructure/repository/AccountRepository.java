package com.mrbt.orbit.ledger.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrbt.orbit.ledger.infrastructure.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

	List<AccountEntity> findByUserId(UUID userId);

	boolean existsByUserIdAndName(UUID userId, String name);

}
