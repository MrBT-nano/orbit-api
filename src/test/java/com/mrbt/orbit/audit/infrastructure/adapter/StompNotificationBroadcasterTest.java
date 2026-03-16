package com.mrbt.orbit.audit.infrastructure.adapter;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;

@ExtendWith(MockitoExtension.class)
class StompNotificationBroadcasterTest {

	@Mock
	private SimpMessagingTemplate messagingTemplate;

	@InjectMocks
	private StompNotificationBroadcaster broadcaster;

	@Test
	void broadcast_sendsToCorrectDestination() {
		UUID userId = UUID.randomUUID();
		Notification notification = Notification.builder().id(UUID.randomUUID()).userId(userId)
				.type(NotificationType.SYSTEM).title("Test").message("Hello").channel(NotificationChannel.IN_APP)
				.isRead(false).build();

		broadcaster.broadcast(notification);

		verify(messagingTemplate).convertAndSend("/topic/notifications/" + userId, notification);
	}

}
