package com.mrbt.orbit.payment.core.model;

import java.util.UUID;

import com.mrbt.orbit.common.core.model.BaseDomainModel;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentMethod extends BaseDomainModel {

	private UUID userId;

	private PaymentProvider provider;

	private String providerReferenceId;

	private String lastFourDigits;

	private Boolean isDefault;

	private PaymentMethodStatus status;

}
