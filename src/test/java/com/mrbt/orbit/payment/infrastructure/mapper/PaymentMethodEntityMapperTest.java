package com.mrbt.orbit.payment.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;
import com.mrbt.orbit.payment.infrastructure.entity.PaymentMethodEntity;

class PaymentMethodEntityMapperTest {

	private final PaymentMethodEntityMapper mapper = new PaymentMethodEntityMapper();

	@Test
	void toDomain_mapsAllFields() {
		PaymentMethodEntity entity = new PaymentMethodEntity();
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setProvider(PaymentProvider.STRIPE);
		entity.setProviderReferenceId("pm_123abc");
		entity.setLastFourDigits("4242");
		entity.setIsDefault(true);
		entity.setStatus(PaymentMethodStatus.ACTIVE);
		entity.setCreatedAt(Instant.parse("2026-01-01T00:00:00Z"));
		entity.setUpdatedAt(Instant.parse("2026-01-02T00:00:00Z"));

		PaymentMethod result = mapper.toDomain(entity);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getProvider()).isEqualTo(PaymentProvider.STRIPE);
		assertThat(result.getProviderReferenceId()).isEqualTo("pm_123abc");
		assertThat(result.getLastFourDigits()).isEqualTo("4242");
		assertThat(result.getIsDefault()).isTrue();
		assertThat(result.getStatus()).isEqualTo(PaymentMethodStatus.ACTIVE);
		assertThat(result.getCreatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
		assertThat(result.getUpdatedAt()).isEqualTo(OffsetDateTime.of(2026, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain((PaymentMethodEntity) null)).isNull();
	}

	@Test
	void toDomain_handlesNullTimestamps() {
		PaymentMethodEntity entity = new PaymentMethodEntity();
		entity.setProvider(PaymentProvider.STRIPE);
		entity.setStatus(PaymentMethodStatus.ACTIVE);

		PaymentMethod result = mapper.toDomain(entity);

		assertThat(result.getCreatedAt()).isNull();
		assertThat(result.getUpdatedAt()).isNull();
	}

	@Test
	void toEntity_mapsAllFields() {
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		PaymentMethod domain = PaymentMethod.builder().id(id).userId(userId).provider(PaymentProvider.STRIPE)
				.providerReferenceId("pm_456def").lastFourDigits("1234").isDefault(false)
				.status(PaymentMethodStatus.ACTIVE).build();

		PaymentMethodEntity result = mapper.toEntity(domain);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getProvider()).isEqualTo(PaymentProvider.STRIPE);
		assertThat(result.getProviderReferenceId()).isEqualTo("pm_456def");
		assertThat(result.getLastFourDigits()).isEqualTo("1234");
		assertThat(result.getIsDefault()).isFalse();
		assertThat(result.getStatus()).isEqualTo(PaymentMethodStatus.ACTIVE);
	}

	@Test
	void toEntity_returnsNullForNull() {
		assertThat(mapper.toEntity(null)).isNull();
	}

}
