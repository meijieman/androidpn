package org.androidpn.server.service;

import org.androidpn.server.model.Notification;

import java.util.List;

public interface NotificationService {

	void saveNotification(Notification notification);

	List<Notification> findNotificationsByUsername(String username);

	void deleteNotification(Notification notification);

	void deleteNotification(String uuid);

	List<Notification> getNotifications();

	Notification getNotificationByUuid(String UUID);
}
