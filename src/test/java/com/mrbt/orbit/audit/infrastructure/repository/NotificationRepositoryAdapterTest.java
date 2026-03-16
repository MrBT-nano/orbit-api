package com.mrbt.orbit.audit.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;
import com.mrbt.orbit.audit.infrastructure.entity.NotificationEntity;
import com.mrbt.orbit.audit.infrastructure.mapper.NotificationEntityMapper;
import com.mrbt.orbit.common.core.model.PageResult;

@ExtendWith(MockitoExtension.class)
class NotificationRepositoryAdapterTest {

	@Mock
	private NotificationRepository springDataRepository;

	@Mock
	private NotificationEntityMapper mapper;

	@InjectMocks
	private NotificationRepositoryAdapter adapter;

	@Test
	void save_convertsAndPersists() {
		Notification domain = Notification.builder().title("Test").type(NotificationType.SYSTEM).build();
		NotificationEntity entity = new NotificationEntity();
		NotificationEntity savedEntity = new NotificationEntity();
		Notification expected = Notification.builder().id(UUID.randomUUID()).title("Test").build();

		when(mapper.toEntity(domain)).thenReturn(entity);
		when(springDataRepository.save(entity)).thenReturn(savedEntity);
		when(mapper.toDomain(savedEntity)).thenReturn(expected);

		assertThat(adapter.save(domain)).isEqualTo(expected);
	}

	@Test
	void findById_returnsNotification() {
		UUID id = UUID.randomUUID();
		NotificationEntity entity = new NotificationEntity();
		Notification expected = Notification.builder().id(id).build();

		when(springDataRepository.findById(id)).thenReturn(Optional.of(entity));
		when(mapper.toDomain(entity)).thenReturn(expected);

		assertThat(adapter.findById(id)).isPresent().contains(expected);
	}

	@Test
	void findByUserId_returnsPageResult() {
		UUID userId = UUID.randomUUID();
		NotificationEntity entity = new NotificationEntity();
		Page<NotificationEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
		Notification mapped = Notification.builder().id(UUID.randomUUID()).build();

		when(springDataRepository.findByUserIdOrderByCreatedAtDesc(any(UUID.class), any(PageRequest.class)))
				.thenReturn(page);
		when(mapper.toDomainList(List.of(entity))).thenReturn(List.of(mapped));

		PageResult<Notification> result = adapter.findByUserId(userId, 0, 10);

		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1);
		assertThat(result.totalPages()).isEqualTo(1);
	}

	@Test
	void countUnreadByUserId_returnsCount() {
		UUID userId = UUID.randomUUID();
		when(springDataRepository.countByUserIdAndIsReadFalse(userId)).thenReturn(5L);

		assertThat(adapter.countUnreadByUserId(userId)).isEqualTo(5L);
	}

	@Test
	void markAsReadById_delegatesToRepo() {
		UUID id = UUID.randomUUID();
		adapter.markAsReadById(id);
		verify(springDataRepository).markAsReadById(id);
	}

	@Test
	void markAllAsReadByUserId_delegatesToRepo() {
		UUID userId = UUID.randomUUID();
		adapter.markAllAsReadByUserId(userId);
		verify(springDataRepository).markAllAsReadByUserId(userId);
	}

}
