package com.mrbt.orbit.audit.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.infrastructure.entity.NotificationEntity;

class NotificationEntityMapperTest {

	private final NotificationEntityMapper mapper = new NotificationEntityMapper();

	@Test
	void toDomain_mapsAllFields() {
		NotificationEntity entity = new NotificationEntity();
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setType(NotificationType.BUDGET_ALERT);
		entity.setTitle("Budget exceeded");
		entity.setMessage("You have exceeded your food budget");
		entity.setChannel(NotificationChannel.IN_APP);
		entity.setIsRead(false);
		entity.setCreatedAt(Instant.parse("2026-01-15T12:00:00Z"));

		Notification result = mapper.toDomain(entity);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getType()).isEqualTo(NotificationType.BUDGET_ALERT);
		assertThat(result.getTitle()).isEqualTo("Budget exceeded");
		assertThat(result.getMessage()).isEqualTo("You have exceeded your food budget");
		assertThat(result.getChannel()).isEqualTo(NotificationChannel.IN_APP);
		assertThat(result.getIsRead()).isFalse();
	}

	@Test
	void toDomain_returnsNullForNull() {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void toEntity_mapsAllFields() {
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Notification domain = Notification.builder().id(id).userId(userId).type(NotificationType.LARGE_TRANSACTION)
				.title("Large transaction").message("$5000 withdrawal").channel(NotificationChannel.EMAIL).isRead(true)
				.build();

		NotificationEntity result = mapper.toEntity(domain);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getType()).isEqualTo(NotificationType.LARGE_TRANSACTION);
		assertThat(result.getTitle()).isEqualTo("Large transaction");
		assertThat(result.getChannel()).isEqualTo(NotificationChannel.EMAIL);
		assertThat(result.getIsRead()).isTrue();
	}

	@Test
	void toEntity_returnsNullForNull() {
		assertThat(mapper.toEntity(null)).isNull();
	}

	@Test
	void toDomainList_convertsList() {
		NotificationEntity e1 = new NotificationEntity();
		e1.setId(UUID.randomUUID());
		e1.setTitle("N1");
		e1.setType(NotificationType.SYSTEM);

		NotificationEntity e2 = new NotificationEntity();
		e2.setId(UUID.randomUUID());
		e2.setTitle("N2");
		e2.setType(NotificationType.BILL_REMINDER);

		List<Notification> result = mapper.toDomainList(List.of(e1, e2));

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getTitle()).isEqualTo("N1");
	}

	@Test
	void toDomainList_returnsNullForNull() {
		assertThat(mapper.toDomainList(null)).isNull();
	}

	@Test
	void toEntityList_convertsList() {
		Notification n1 = Notification.builder().id(UUID.randomUUID()).title("N1").type(NotificationType.SYSTEM)
				.build();
		Notification n2 = Notification.builder().id(UUID.randomUUID()).title("N2").type(NotificationType.GOAL_MILESTONE)
				.build();

		List<NotificationEntity> result = mapper.toEntityList(List.of(n1, n2));

		assertThat(result).hasSize(2);
	}

	@Test
	void toEntityList_returnsNullForNull() {
		assertThat(mapper.toEntityList(null)).isNull();
	}

}
