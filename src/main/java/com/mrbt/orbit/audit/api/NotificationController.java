package com.mrbt.orbit.audit.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrbt.orbit.audit.api.mapper.NotificationDtoMapper;
import com.mrbt.orbit.audit.api.response.NotificationResponse;
import com.mrbt.orbit.audit.core.port.in.GetNotificationsUseCase;
import com.mrbt.orbit.audit.core.port.in.MarkNotificationReadUseCase;
import com.mrbt.orbit.common.api.ApiResponse;
import com.mrbt.orbit.common.api.PaginationParams;
import com.mrbt.orbit.common.core.model.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "User notification management")
public class NotificationController {

	private final GetNotificationsUseCase getNotificationsUseCase;

	private final MarkNotificationReadUseCase markNotificationReadUseCase;

	private final NotificationDtoMapper dtoMapper;

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get notifications for a user", description = "Returns paginated notifications for a user, newest first")
	public ResponseEntity<ApiResponse<PageResult<NotificationResponse>>> getNotifications(@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		PaginationParams pagination = PaginationParams.of(page, size);
		var result = getNotificationsUseCase.getNotificationsByUserId(userId, pagination.page(), pagination.size());

		var responseResult = new PageResult<>(dtoMapper.toResponseList(result.content()), result.totalElements(),
				result.totalPages(), result.page(), result.size());

		return ResponseEntity.ok(ApiResponse.success(responseResult));
	}

	@GetMapping("/user/{userId}/unread-count")
	@Operation(summary = "Get unread notification count", description = "Returns the number of unread notifications for a user")
	public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable UUID userId) {
		long count = getNotificationsUseCase.getUnreadCount(userId);
		return ResponseEntity.ok(ApiResponse.success(count));
	}

	@PutMapping("/{id}/read")
	@Operation(summary = "Mark notification as read", description = "Marks a single notification as read")
	public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID id) {
		markNotificationReadUseCase.markAsRead(id);
		return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
	}

	@PutMapping("/user/{userId}/read-all")
	@Operation(summary = "Mark all notifications as read", description = "Marks all unread notifications for a user as read")
	public ResponseEntity<ApiResponse<Void>> markAllAsRead(@PathVariable UUID userId) {
		markNotificationReadUseCase.markAllAsRead(userId);
		return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
	}

}
