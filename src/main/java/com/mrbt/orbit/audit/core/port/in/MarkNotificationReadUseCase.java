package com.mrbt.orbit.audit.core.port.in;

import java.util.UUID;

import com.mrbt.orbit.common.core.port.in.UseCase;

public interface MarkNotificationReadUseCase extends UseCase {

	void markAsRead(UUID notificationId);

	void markAllAsRead(UUID userId);

}
