package com.mrbt.orbit.audit.core.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.port.in.CreateNotificationUseCase;
import com.mrbt.orbit.audit.core.port.in.GetNotificationsUseCase;
import com.mrbt.orbit.audit.core.port.in.MarkNotificationReadUseCase;
import com.mrbt.orbit.audit.core.port.out.NotificationBroadcastPort;
import com.mrbt.orbit.audit.core.port.out.NotificationRepositoryPort;
import com.mrbt.orbit.common.core.model.PageResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService
		implements
			CreateNotificationUseCase,
			GetNotificationsUseCase,
			MarkNotificationReadUseCase {

	private final NotificationRepositoryPort repositoryPort;

	private final NotificationBroadcastPort broadcastPort;

	@Override
	@Transactional
	public Notification createNotification(Notification notification) {
		Notification saved = repositoryPort.save(notification);
		broadcastPort.broadcast(saved);
		return saved;
	}

	@Override
	@Transactional(readOnly = true)
	public PageResult<Notification> getNotificationsByUserId(UUID userId, int page, int size) {
		return repositoryPort.findByUserId(userId, page, size);
	}

	@Override
	@Transactional(readOnly = true)
	public long getUnreadCount(UUID userId) {
		return repositoryPort.countUnreadByUserId(userId);
	}

	@Override
	@Transactional
	public void markAsRead(UUID notificationId) {
		repositoryPort.markAsReadById(notificationId);
	}

	@Override
	@Transactional
	public void markAllAsRead(UUID userId) {
		repositoryPort.markAllAsReadByUserId(userId);
	}

}
