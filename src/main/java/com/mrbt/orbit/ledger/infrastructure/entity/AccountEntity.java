package com.mrbt.orbit.ledger.infrastructure.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.mrbt.orbit.common.infrastructure.entity.SoftDeletableEntity;
import com.mrbt.orbit.ledger.core.model.enums.AccountStatus;
import com.mrbt.orbit.ledger.core.model.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class AccountEntity extends SoftDeletableEntity {

	@Column(nullable = false)
	private UUID userId;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType type;

	@Column(nullable = false)
	private String currencyCode;

	@Column(precision = 19, scale = 4)
	private BigDecimal currentBalance;

	private String plaidAccountId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;

	@Version
	private Long version;

}
