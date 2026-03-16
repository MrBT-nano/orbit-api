package com.mrbt.orbit.audit.api;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mrbt.orbit.audit.api.mapper.NotificationDtoMapper;
import com.mrbt.orbit.audit.api.response.NotificationResponse;
import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.core.port.in.GetNotificationsUseCase;
import com.mrbt.orbit.audit.core.port.in.MarkNotificationReadUseCase;
import com.mrbt.orbit.common.core.model.PageResult;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GetNotificationsUseCase getNotificationsUseCase;

	@MockitoBean
	private MarkNotificationReadUseCase markNotificationReadUseCase;

	@MockitoBean
	private NotificationDtoMapper dtoMapper;

	@Test
	void getNotifications_returnsPage() throws Exception {
		UUID userId = UUID.randomUUID();
		Notification notification = Notification.builder().id(UUID.randomUUID()).userId(userId)
				.type(NotificationType.SYSTEM).title("Test").message("Hello").channel(NotificationChannel.IN_APP)
				.isRead(false).createdAt(OffsetDateTime.now()).build();

		var pageResult = new PageResult<>(List.of(notification), 1L, 1, 0, 20);
		when(getNotificationsUseCase.getNotificationsByUserId(eq(userId), anyInt(), anyInt())).thenReturn(pageResult);

		var response = NotificationResponse.builder().id(notification.getId()).userId(userId).type("SYSTEM")
				.title("Test").message("Hello").channel("IN_APP").isRead(false).build();
		when(dtoMapper.toResponseList(List.of(notification))).thenReturn(List.of(response));

		mockMvc.perform(get("/api/v1/notifications/user/{userId}", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	void getUnreadCount_returnsCount() throws Exception {
		UUID userId = UUID.randomUUID();
		when(getNotificationsUseCase.getUnreadCount(userId)).thenReturn(3L);

		mockMvc.perform(get("/api/v1/notifications/user/{userId}/unread-count", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data").value(3));
	}

	@Test
	void markAsRead_returns200() throws Exception {
		UUID id = UUID.randomUUID();

		mockMvc.perform(put("/api/v1/notifications/{id}/read", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));

		verify(markNotificationReadUseCase).markAsRead(id);
	}

	@Test
	void markAllAsRead_returns200() throws Exception {
		UUID userId = UUID.randomUUID();

		mockMvc.perform(put("/api/v1/notifications/user/{userId}/read-all", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));

		verify(markNotificationReadUseCase).markAllAsRead(userId);
	}

}
