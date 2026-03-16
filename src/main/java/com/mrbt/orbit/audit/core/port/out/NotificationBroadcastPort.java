package com.mrbt.orbit.audit.core.port.out;

import com.mrbt.orbit.audit.core.model.Notification;

public interface NotificationBroadcastPort {

	void broadcast(Notification notification);

}
