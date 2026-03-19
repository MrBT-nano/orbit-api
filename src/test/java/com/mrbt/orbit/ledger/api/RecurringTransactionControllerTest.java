package com.mrbt.orbit.ledger.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mrbt.orbit.common.core.model.PageResult;
import com.mrbt.orbit.ledger.api.request.CreateRecurringTransactionRequest;
import com.mrbt.orbit.ledger.api.request.UpdateRecurringTransactionRequest;
import com.mrbt.orbit.ledger.api.response.RecurringTransactionResponse;
import com.mrbt.orbit.ledger.core.model.RecurringTransaction;
import com.mrbt.orbit.ledger.core.model.enums.Frequency;
import com.mrbt.orbit.ledger.core.model.enums.RecurringTransactionStatus;
import com.mrbt.orbit.ledger.core.port.in.CreateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.DeleteRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.GetRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.core.port.in.UpdateRecurringTransactionUseCase;
import com.mrbt.orbit.ledger.infrastructure.mapper.RecurringTransactionMapper;

@WebMvcTest(RecurringTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecurringTransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@MockitoBean
	private CreateRecurringTransactionUseCase createUseCase;

	@MockitoBean
	private GetRecurringTransactionUseCase getUseCase;

	@MockitoBean
	private UpdateRecurringTransactionUseCase updateUseCase;

	@MockitoBean
	private DeleteRecurringTransactionUseCase deleteUseCase;

	@MockitoBean
	private RecurringTransactionMapper mapper;

	private RecurringTransactionResponse buildResponse(UUID id) {
		return RecurringTransactionResponse.builder().id(id).userId(UUID.randomUUID()).accountId(UUID.randomUUID())
				.categoryId(UUID.randomUUID()).amount(new BigDecimal("100.00")).currencyCode("USD")
				.description("Monthly rent").frequency("MONTHLY").nextOccurrence(LocalDate.now()).autoConfirm(true)
				.status("ACTIVE").build();
	}

	@Test
	void create_returns201() throws Exception {
		UUID id = UUID.randomUUID();
		RecurringTransactionResponse response = buildResponse(id);

		when(createUseCase.create(any(RecurringTransaction.class))).thenAnswer(inv -> {
			RecurringTransaction r = inv.getArgument(0);
			r.setId(id);
			r.setStatus(RecurringTransactionStatus.ACTIVE);
			r.setAutoConfirm(true);
			return r;
		});
		when(mapper.toResponse(any(RecurringTransaction.class))).thenReturn(response);

		CreateRecurringTransactionRequest request = CreateRecurringTransactionRequest.builder()
				.userId(UUID.randomUUID()).accountId(UUID.randomUUID()).categoryId(UUID.randomUUID())
				.amount(new BigDecimal("100.00")).currencyCode("USD").description("Monthly rent")
				.frequency(Frequency.MONTHLY).startDate(LocalDate.now()).autoConfirm(true).build();

		mockMvc.perform(post("/api/v1/recurring-transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.description").value("Monthly rent"));
	}

	@Test
	void getById_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		RecurringTransaction recurring = RecurringTransaction.builder().id(id).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).amount(new BigDecimal("100.00")).currencyCode("USD")
				.frequency(Frequency.MONTHLY).nextOccurrence(LocalDate.now()).status(RecurringTransactionStatus.ACTIVE)
				.build();
		RecurringTransactionResponse response = buildResponse(id);

		when(getUseCase.findById(id)).thenReturn(Optional.of(recurring));
		when(mapper.toResponse(any(RecurringTransaction.class))).thenReturn(response);

		mockMvc.perform(get("/api/v1/recurring-transactions/{id}", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.id").value(id.toString()));
	}

	@Test
	void getById_returns404WhenNotFound() throws Exception {
		UUID id = UUID.randomUUID();

		when(getUseCase.findById(id)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/recurring-transactions/{id}", id)).andExpect(status().isNotFound());
	}

	@Test
	void getByUserId_returnsPage() throws Exception {
		UUID userId = UUID.randomUUID();
		RecurringTransaction recurring = RecurringTransaction.builder().id(UUID.randomUUID()).userId(userId)
				.accountId(UUID.randomUUID()).amount(new BigDecimal("100.00")).currencyCode("USD")
				.frequency(Frequency.MONTHLY).nextOccurrence(LocalDate.now()).status(RecurringTransactionStatus.ACTIVE)
				.build();
		PageResult<RecurringTransaction> page = new PageResult<>(List.of(recurring), 1L, 1, 0, 20);
		RecurringTransactionResponse response = buildResponse(recurring.getId());

		when(getUseCase.findByUserId(eq(userId), anyInt(), anyInt())).thenReturn(page);
		when(mapper.toResponseList(any())).thenReturn(List.of(response));

		mockMvc.perform(get("/api/v1/recurring-transactions/user/{userId}", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.content").isArray())
				.andExpect(jsonPath("$.data.totalElements").value(1)).andExpect(jsonPath("$.data.page").value(0))
				.andExpect(jsonPath("$.data.size").value(20));
	}

	@Test
	void update_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		RecurringTransaction updated = RecurringTransaction.builder().id(id).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).amount(new BigDecimal("200.00")).currencyCode("USD")
				.description("Updated rent").frequency(Frequency.MONTHLY).nextOccurrence(LocalDate.now())
				.status(RecurringTransactionStatus.ACTIVE).build();
		RecurringTransactionResponse response = RecurringTransactionResponse.builder().id(id)
				.description("Updated rent").amount(new BigDecimal("200.00")).status("ACTIVE").build();

		when(updateUseCase.update(eq(id), eq("Updated rent"), isNull(), isNull())).thenReturn(updated);
		when(mapper.toResponse(any(RecurringTransaction.class))).thenReturn(response);

		UpdateRecurringTransactionRequest request = UpdateRecurringTransactionRequest.builder()
				.description("Updated rent").build();

		mockMvc.perform(patch("/api/v1/recurring-transactions/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.description").value("Updated rent"));
	}

	@Test
	void togglePause_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		RecurringTransaction paused = RecurringTransaction.builder().id(id).userId(UUID.randomUUID())
				.accountId(UUID.randomUUID()).amount(new BigDecimal("100.00")).currencyCode("USD")
				.frequency(Frequency.MONTHLY).nextOccurrence(LocalDate.now()).status(RecurringTransactionStatus.PAUSED)
				.build();
		RecurringTransactionResponse response = RecurringTransactionResponse.builder().id(id).status("PAUSED").build();

		when(updateUseCase.togglePause(id)).thenReturn(paused);
		when(mapper.toResponse(any(RecurringTransaction.class))).thenReturn(response);

		mockMvc.perform(patch("/api/v1/recurring-transactions/{id}/pause", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.status").value("PAUSED"));
	}

	@Test
	void delete_returns200() throws Exception {
		UUID id = UUID.randomUUID();

		doNothing().when(deleteUseCase).cancel(id);

		mockMvc.perform(delete("/api/v1/recurring-transactions/{id}", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

}
