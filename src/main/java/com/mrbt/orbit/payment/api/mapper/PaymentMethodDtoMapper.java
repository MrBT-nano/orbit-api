package com.mrbt.orbit.payment.api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.payment.api.request.CreatePaymentMethodRequest;
import com.mrbt.orbit.payment.api.response.PaymentMethodResponse;
import com.mrbt.orbit.payment.core.model.PaymentMethod;

@Component
public class PaymentMethodDtoMapper {

	public PaymentMethod toDomain(CreatePaymentMethodRequest request) {
		if (request == null)
			return null;

		return PaymentMethod.builder().userId(request.userId()).provider(request.provider())
				.providerReferenceId(request.providerReferenceId()).lastFourDigits(request.lastFourDigits())
				.isDefault(request.isDefault()).build();
	}

	public PaymentMethodResponse toResponse(PaymentMethod domain) {
		if (domain == null)
			return null;

		return PaymentMethodResponse.builder().id(domain.getId()).userId(domain.getUserId())
				.provider(domain.getProvider() != null ? domain.getProvider().name() : null)
				.providerReferenceId(domain.getProviderReferenceId()).lastFourDigits(domain.getLastFourDigits())
				.isDefault(domain.getIsDefault()).status(domain.getStatus() != null ? domain.getStatus().name() : null)
				.createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt()).build();
	}

	public List<PaymentMethodResponse> toResponseList(List<PaymentMethod> domains) {
		if (domains == null)
			return null;
		return domains.stream().map(this::toResponse).toList();
	}

}
