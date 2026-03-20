package com.mrbt.orbit.payment.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.api.mapper.PaymentMethodDtoMapper;
import com.mrbt.orbit.payment.api.request.CreatePaymentMethodRequest;
import com.mrbt.orbit.payment.api.request.UpdatePaymentMethodRequest;
import com.mrbt.orbit.payment.core.model.PaymentMethod;
import com.mrbt.orbit.payment.core.model.enums.PaymentMethodStatus;
import com.mrbt.orbit.payment.core.model.enums.PaymentProvider;
import com.mrbt.orbit.payment.core.port.in.CreatePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.DeletePaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.GetPaymentMethodUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdatePaymentMethodUseCase;

@WebMvcTest(PaymentMethodController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PaymentMethodDtoMapper.class)
class PaymentMethodControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockitoBean
	private CreatePaymentMethodUseCase createPaymentMethodUseCase;

	@MockitoBean
	private GetPaymentMethodUseCase getPaymentMethodUseCase;

	@MockitoBean
	private UpdatePaymentMethodUseCase updatePaymentMethodUseCase;

	@MockitoBean
	private DeletePaymentMethodUseCase deletePaymentMethodUseCase;

	@Test
	void create_returns201() throws Exception {
		UUID pmId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();

		when(createPaymentMethodUseCase.create(any(PaymentMethod.class))).thenAnswer(inv -> {
			PaymentMethod pm = inv.getArgument(0);
			pm.setId(pmId);
			pm.setStatus(PaymentMethodStatus.ACTIVE);
			pm.setIsDefault(false);
			return pm;
		});

		CreatePaymentMethodRequest request = CreatePaymentMethodRequest.builder().userId(userId)
				.provider(PaymentProvider.STRIPE).lastFourDigits("4242").build();

		mockMvc.perform(post("/api/v1/payment-methods").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.lastFourDigits").value("4242"));
	}

	@Test
	void getById_returns200() throws Exception {
		UUID pmId = UUID.randomUUID();
		PaymentMethod pm = PaymentMethod.builder().id(pmId).provider(PaymentProvider.STRIPE)
				.status(PaymentMethodStatus.ACTIVE).isDefault(false).build();

		when(getPaymentMethodUseCase.findById(pmId)).thenReturn(Optional.of(pm));

		mockMvc.perform(get("/api/v1/payment-methods/{id}", pmId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(pmId.toString()));
	}

	@Test
	void getById_returns404() throws Exception {
		UUID pmId = UUID.randomUUID();
		when(getPaymentMethodUseCase.findById(pmId)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/payment-methods/{id}", pmId)).andExpect(status().isNotFound());
	}

	@Test
	void getByUserId_returnsPage() throws Exception {
		UUID userId = UUID.randomUUID();
		PaymentMethod pm = PaymentMethod.builder().id(UUID.randomUUID()).userId(userId).provider(PaymentProvider.STRIPE)
				.status(PaymentMethodStatus.ACTIVE).build();
		PageResult<PaymentMethod> page = new PageResult<>(List.of(pm), 1L, 1, 0, 20);

		when(getPaymentMethodUseCase.findByUserId(eq(userId), anyInt(), anyInt())).thenReturn(page);

		mockMvc.perform(get("/api/v1/payment-methods/user/{userId}", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.content").isArray())
				.andExpect(jsonPath("$.data.totalElements").value(1));
	}

	@Test
	void update_returns200() throws Exception {
		UUID pmId = UUID.randomUUID();
		PaymentMethod updated = PaymentMethod.builder().id(pmId).provider(PaymentProvider.STRIPE)
				.status(PaymentMethodStatus.ACTIVE).isDefault(true).build();

		when(updatePaymentMethodUseCase.update(eq(pmId), eq(true))).thenReturn(updated);

		UpdatePaymentMethodRequest request = UpdatePaymentMethodRequest.builder().isDefault(true).build();

		mockMvc.perform(patch("/api/v1/payment-methods/{id}", pmId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.isDefault").value(true));
	}

	@Test
	void delete_returns200() throws Exception {
		UUID pmId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/payment-methods/{id}", pmId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

}
