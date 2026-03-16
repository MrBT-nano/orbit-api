package com.mrbt.orbit.audit.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.mrbt.orbit.audit.api.response.NotificationResponse;
import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.audit.core.model.enums.NotificationChannel;
import com.mrbt.orbit.audit.core.model.enums.NotificationType;

class NotificationDtoMapperTest {

	private final NotificationDtoMapper mapper = new NotificationDtoMapper();

	@Test
	void toResponse_mapsAllFields() {
		UUID id = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		OffsetDateTime now = OffsetDateTime.now();

		Notification domain = Notification.builder().id(id).userId(userId).type(NotificationType.BUDGET_ALERT)
				.title("Budget exceeded").message("You exceeded your food budget").channel(NotificationChannel.IN_APP)
				.isRead(false).createdAt(now).build();

		NotificationResponse result = mapper.toResponse(domain);

		assertThat(result.id()).isEqualTo(id);
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.type()).isEqualTo("BUDGET_ALERT");
		assertThat(result.title()).isEqualTo("Budget exceeded");
		assertThat(result.message()).isEqualTo("You exceeded your food budget");
		assertThat(result.channel()).isEqualTo("IN_APP");
		assertThat(result.isRead()).isFalse();
		assertThat(result.createdAt()).isEqualTo(now);
	}

	@Test
	void toResponse_returnsNullForNull() {
		assertThat(mapper.toResponse(null)).isNull();
	}

	@Test
	void toResponse_handlesNullTypeAndChannel() {
		Notification domain = Notification.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).title("Test")
				.message("Hello").isRead(true).build();

		NotificationResponse result = mapper.toResponse(domain);

		assertThat(result.type()).isNull();
		assertThat(result.channel()).isNull();
	}

	@Test
	void toResponseList_convertsList() {
		Notification n1 = Notification.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.type(NotificationType.SYSTEM).title("N1").message("M1").channel(NotificationChannel.IN_APP)
				.isRead(false).build();

		Notification n2 = Notification.builder().id(UUID.randomUUID()).userId(UUID.randomUUID())
				.type(NotificationType.BILL_REMINDER).title("N2").message("M2").channel(NotificationChannel.EMAIL)
				.isRead(true).build();

		List<NotificationResponse> result = mapper.toResponseList(List.of(n1, n2));

		assertThat(result).hasSize(2);
		assertThat(result.get(0).title()).isEqualTo("N1");
		assertThat(result.get(1).title()).isEqualTo("N2");
	}

	@Test
	void toResponseList_returnsNullForNull() {
		assertThat(mapper.toResponseList(null)).isNull();
	}

}
