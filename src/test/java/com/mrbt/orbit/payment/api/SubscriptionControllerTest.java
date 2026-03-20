package com.mrbt.orbit.payment.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.payment.api.mapper.SubscriptionDtoMapper;
import com.mrbt.orbit.payment.api.request.CreateSubscriptionRequest;
import com.mrbt.orbit.payment.api.request.UpdateSubscriptionRequest;
import com.mrbt.orbit.payment.core.model.Subscription;
import com.mrbt.orbit.payment.core.model.enums.BillingCycle;
import com.mrbt.orbit.payment.core.model.enums.SubscriptionStatus;
import com.mrbt.orbit.payment.core.port.in.CreateSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.DeleteSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.GetSubscriptionUseCase;
import com.mrbt.orbit.payment.core.port.in.UpdateSubscriptionUseCase;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SubscriptionDtoMapper.class)
class SubscriptionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@MockitoBean
	private CreateSubscriptionUseCase createSubscriptionUseCase;

	@MockitoBean
	private GetSubscriptionUseCase getSubscriptionUseCase;

	@MockitoBean
	private UpdateSubscriptionUseCase updateSubscriptionUseCase;

	@MockitoBean
	private DeleteSubscriptionUseCase deleteSubscriptionUseCase;

	@Test
	void create_returns201() throws Exception {
		UUID subId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		UUID accountId = UUID.randomUUID();

		when(createSubscriptionUseCase.create(any(Subscription.class))).thenAnswer(inv -> {
			Subscription sub = inv.getArgument(0);
			sub.setId(subId);
			sub.setStatus(SubscriptionStatus.ACTIVE);
			return sub;
		});

		CreateSubscriptionRequest request = CreateSubscriptionRequest.builder().userId(userId).accountId(accountId)
				.name("Netflix").amount(new BigDecimal("15.99")).currencyCode("USD").billingCycle(BillingCycle.MONTHLY)
				.nextBillingDate(LocalDate.of(2026, 4, 1)).build();

		mockMvc.perform(post("/api/v1/subscriptions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.name").value("Netflix"));
	}

	@Test
	void getById_returns200() throws Exception {
		UUID subId = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(subId).name("Netflix").status(SubscriptionStatus.ACTIVE)
				.amount(new BigDecimal("15.99")).billingCycle(BillingCycle.MONTHLY).build();

		when(getSubscriptionUseCase.findById(subId)).thenReturn(Optional.of(sub));

		mockMvc.perform(get("/api/v1/subscriptions/{id}", subId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(subId.toString()));
	}

	@Test
	void getById_returns404() throws Exception {
		UUID subId = UUID.randomUUID();
		when(getSubscriptionUseCase.findById(subId)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/subscriptions/{id}", subId)).andExpect(status().isNotFound());
	}

	@Test
	void getByUserId_returnsPage() throws Exception {
		UUID userId = UUID.randomUUID();
		Subscription sub = Subscription.builder().id(UUID.randomUUID()).userId(userId).name("Netflix")
				.status(SubscriptionStatus.ACTIVE).build();
		PageResult<Subscription> page = new PageResult<>(List.of(sub), 1L, 1, 0, 20);

		when(getSubscriptionUseCase.findByUserId(eq(userId), anyInt(), anyInt())).thenReturn(page);

		mockMvc.perform(get("/api/v1/subscriptions/user/{userId}", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.content").isArray())
				.andExpect(jsonPath("$.data.totalElements").value(1));
	}

	@Test
	void update_returns200() throws Exception {
		UUID subId = UUID.randomUUID();
		Subscription updated = Subscription.builder().id(subId).name("Netflix Premium")
				.status(SubscriptionStatus.ACTIVE).amount(new BigDecimal("22.99")).build();

		when(updateSubscriptionUseCase.update(eq(subId), eq("Netflix Premium"), isNull(), isNull()))
				.thenReturn(updated);

		UpdateSubscriptionRequest request = UpdateSubscriptionRequest.builder().name("Netflix Premium").build();

		mockMvc.perform(patch("/api/v1/subscriptions/{id}", subId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.name").value("Netflix Premium"));
	}

	@Test
	void togglePause_returns200() throws Exception {
		UUID subId = UUID.randomUUID();
		Subscription toggled = Subscription.builder().id(subId).name("Netflix").status(SubscriptionStatus.PAUSED)
				.build();

		when(updateSubscriptionUseCase.togglePause(subId)).thenReturn(toggled);

		mockMvc.perform(patch("/api/v1/subscriptions/{id}/pause", subId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.status").value("PAUSED"));
	}

	@Test
	void delete_returns200() throws Exception {
		UUID subId = UUID.randomUUID();

		mockMvc.perform(delete("/api/v1/subscriptions/{id}", subId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

}
