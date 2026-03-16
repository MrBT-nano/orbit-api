package com.mrbt.orbit.audit.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.infrastructure.entity.NotificationEntity;
import com.mrbt.orbit.config.JpaAuditingConfig;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class NotificationRepositoryTest {

	@Autowired
	private NotificationRepository repository;

	@Autowired
	private EntityManager entityManager;

	private NotificationEntity createEntity(UUID userId, boolean isRead) {
		NotificationEntity entity = new NotificationEntity();
		entity.setUserId(userId);
		entity.setType(NotificationType.SYSTEM);
		entity.setTitle("Test");
		entity.setMessage("Hello");
		entity.setChannel(NotificationChannel.IN_APP);
		entity.setIsRead(isRead);
		return repository.save(entity);
	}

	@Test
	void findByUserIdOrderByCreatedAtDesc_returnsUserNotifications() {
		UUID userId = UUID.randomUUID();
		createEntity(userId, false);
		createEntity(userId, true);
		createEntity(UUID.randomUUID(), false);

		Page<NotificationEntity> page = repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 10));

		assertThat(page.getTotalElements()).isEqualTo(2);
	}

	@Test
	void countByUserIdAndIsReadFalse_returnsUnreadCount() {
		UUID userId = UUID.randomUUID();
		createEntity(userId, false);
		createEntity(userId, false);
		createEntity(userId, true);

		long count = repository.countByUserIdAndIsReadFalse(userId);

		assertThat(count).isEqualTo(2);
	}

	@Test
	void markAsReadById_updatesEntity() {
		UUID userId = UUID.randomUUID();
		NotificationEntity entity = createEntity(userId, false);

		repository.markAsReadById(entity.getId());
		repository.flush();
		entityManager.clear();

		NotificationEntity updated = repository.findById(entity.getId()).orElseThrow();
		assertThat(updated.getIsRead()).isTrue();
	}

	@Test
	void markAllAsReadByUserId_updatesAllUnread() {
		UUID userId = UUID.randomUUID();
		createEntity(userId, false);
		createEntity(userId, false);

		repository.markAllAsReadByUserId(userId);
		repository.flush();

		long unread = repository.countByUserIdAndIsReadFalse(userId);
		assertThat(unread).isZero();
	}

}
