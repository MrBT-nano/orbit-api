package com.mrbt.orbit.audit.infrastructure.mapper;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.infrastructure.entity.NotificationEntity;
import com.mrbt.orbit.common.infrastructure.mapper.AbstractNullSafeMapper;

@Component
public class NotificationEntityMapper extends AbstractNullSafeMapper<NotificationEntity, Notification> {

	@Override
	public Notification toDomain(NotificationEntity entity) {
		if (entity == null) {
			return null;
		}

		return Notification.builder().id(entity.getId()).userId(entity.getUserId()).type(entity.getType())
				.title(entity.getTitle()).message(entity.getMessage()).channel(entity.getChannel())
				.isRead(entity.getIsRead())
				.createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().atOffset(ZoneOffset.UTC) : null)
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().atOffset(ZoneOffset.UTC) : null)
				.build();
	}

	@Override
	public NotificationEntity toEntity(Notification domain) {
		if (domain == null) {
			return null;
		}

		NotificationEntity entity = new NotificationEntity();
		entity.setId(domain.getId());
		entity.setUserId(domain.getUserId());
		entity.setType(domain.getType());
		entity.setTitle(domain.getTitle());
		entity.setMessage(domain.getMessage());
		entity.setChannel(domain.getChannel());
		entity.setIsRead(domain.getIsRead());

		return entity;
	}

}
