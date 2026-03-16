package com.mrbt.orbit.audit.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.core.port.out.NotificationBroadcastPort;
import com.mrbt.orbit.audit.core.port.out.NotificationRepositoryPort;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	private NotificationRepositoryPort repositoryPort;

	@Mock
	private NotificationBroadcastPort broadcastPort;

	@InjectMocks
	private NotificationService service;

	@Test
	void createNotification_savesAndBroadcasts() {
		Notification input = Notification.builder().userId(UUID.randomUUID()).type(NotificationType.SYSTEM)
				.title("Test").message("Hello").channel(NotificationChannel.IN_APP).isRead(false).build();

		when(repositoryPort.save(any())).thenReturn(input);

		Notification result = service.createNotification(input);

		assertThat(result).isNotNull();
		verify(repositoryPort).save(input);
		verify(broadcastPort).broadcast(input);
	}

	@Test
	void markAsRead_callsRepository() {
		UUID id = UUID.randomUUID();
		service.markAsRead(id);
		verify(repositoryPort).markAsReadById(id);
	}

	@Test
	void markAllAsRead_callsRepository() {
		UUID userId = UUID.randomUUID();
		service.markAllAsRead(userId);
		verify(repositoryPort).markAllAsReadByUserId(userId);
	}

	@Test
	void getUnreadCount_returnsCount() {
		UUID userId = UUID.randomUUID();
		when(repositoryPort.countUnreadByUserId(userId)).thenReturn(5L);

		long count = service.getUnreadCount(userId);

		assertThat(count).isEqualTo(5L);
	}

}
