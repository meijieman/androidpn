package org.androidpn.server.dao;

import org.androidpn.server.model.Notification;

import java.util.List;

public interface NotificationDao {

	void saveNotification(Notification notification);
	
	List<Notification> findNotificationsByUsername(String username);
	
	void deleteNotification(Notification notification);
	
	void deleteNotificationByUuid(String UUID);

	List<Notification> getNotifications();

	Notification getNotificationByUuid(String UUID);
}
