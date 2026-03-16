package com.mrbt.orbit.audit.core.port.in;

import com.mrbt.orbit.audit.core.model.Notification;
import com.mrbt.orbit.common.core.port.in.UseCase;

public interface CreateNotificationUseCase extends UseCase {

	Notification createNotification(Notification notification);

}
