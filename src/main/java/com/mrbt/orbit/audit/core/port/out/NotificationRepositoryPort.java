package com.mrbt.orbit.audit.core.port.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrbt.orbit.audit.core.model.Notification;

public interface NotificationRepositoryPort {

	Notification save(Notification notification);

	Optional<Notification> findById(UUID id);

	Page<Notification> findByUserId(UUID userId, Pageable pageable);

	long countUnreadByUserId(UUID userId);

	void markAsReadById(UUID id);

	void markAllAsReadByUserId(UUID userId);

}
