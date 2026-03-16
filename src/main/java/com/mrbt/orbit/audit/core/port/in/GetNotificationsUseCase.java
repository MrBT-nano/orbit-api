package com.mrbt.orbit.audit.core.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.common.core.port.in.UseCase;

public interface GetNotificationsUseCase extends UseCase {

	Page<Notification> getNotificationsByUserId(UUID userId, Pageable pageable);

	long getUnreadCount(UUID userId);

}
