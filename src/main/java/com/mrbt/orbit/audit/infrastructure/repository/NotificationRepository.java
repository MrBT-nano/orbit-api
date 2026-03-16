package com.mrbt.orbit.audit.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mrbt.orbit.audit.infrastructure.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

	Page<NotificationEntity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

	long countByUserIdAndIsReadFalse(UUID userId);

	@Modifying
	@Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.id = :id")
	void markAsReadById(@Param("id") UUID id);

	@Modifying
	@Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.userId = :userId AND n.isRead = false")
	void markAllAsReadByUserId(@Param("userId") UUID userId);

}
