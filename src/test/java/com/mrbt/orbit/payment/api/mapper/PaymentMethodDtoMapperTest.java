package com.mrbt.orbit.payment.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.payment.api.request.CreatePaymentMethodRequest;
import com.mrbt.orbit.payment.api.response.PaymentMethodResponse;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;

class PaymentMethodDtoMapperTest {

	private final PaymentMethodDtoMapper mapper = new PaymentMethodDtoMapper();

	@Test
	void toDomain_fromRequest_mapsAllFields() {
		UUID userId = UUID.randomUUID();
		CreatePaymentMethodRequest request = CreatePaymentMethodRequest.builder().userId(userId)
				.provider(PaymentProvider.STRIPE).providerReferenceId("pm_123").lastFourDigits("4242").isDefault(true)
				.build();

		PaymentMethod result = mapper.toDomain(request);

		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getProvider()).isEqualTo(PaymentProvider.STRIPE);
		assertThat(result.getProviderReferenceId()).isEqualTo("pm_123");
		assertThat(result.getLastFourDigits()).isEqualTo("4242");
		assertThat(result.getIsDefault()).isTrue();
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void toResponse_mapsAllFields() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		PaymentMethod domain = PaymentMethod.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.provider(PaymentProvider.STRIPE).providerReferenceId("pm_456").lastFourDigits("1234").isDefault(false)
				.status(PaymentMethodStatus.ACTIVE).createdAt(now).updatedAt(now).build();

		PaymentMethodResponse result = mapper.toResponse(domain);

		assertThat(result.provider()).isEqualTo("STRIPE");
		assertThat(result.status()).isEqualTo("ACTIVE");
		assertThat(result.lastFourDigits()).isEqualTo("1234");
	}

	@Test
	void toResponse_returnsNullForNull() {
		assertThat(mapper.toResponse(null)).isNull();
	}

	@Test
	void toResponse_handlesNullEnums() {
		PaymentMethod domain = PaymentMethod.builder().lastFourDigits("0000").build();

		PaymentMethodResponse result = mapper.toResponse(domain);

		assertThat(result.provider()).isNull();
		assertThat(result.status()).isNull();
	}

	@Test
	void toResponseList_convertsList() {
		List<PaymentMethod> domains = List.of(
				PaymentMethod.builder().provider(PaymentProvider.STRIPE).status(PaymentMethodStatus.ACTIVE).build(),
				PaymentMethod.builder().provider(PaymentProvider.STRIPE).status(PaymentMethodStatus.EXPIRED).build());

		List<PaymentMethodResponse> result = mapper.toResponseList(domains);

		assertThat(result).hasSize(2);
	}

	@Test
	void toResponseList_returnsNullForNull() {
		assertThat(mapper.toResponseList(null)).isNull();
	}

}
