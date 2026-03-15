package com.mrbt.orbit.ledger.core.model;

import com.mrbt.orbit.common.core.model.BaseDomainModel;
import com.mrbt.orbit.ledger.core.model.enums.AccountStatus;
import com.mrbt.orbit.ledger.core.model.enums.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Account extends BaseDomainModel {
	private UUID userId;
	private String name;
	private AccountType type;
	private String currencyCode;
	private BigDecimal currentBalance;
	private String plaidAccountId;
	private AccountStatus status;
}
