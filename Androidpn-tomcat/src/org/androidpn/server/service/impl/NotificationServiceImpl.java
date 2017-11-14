package org.androidpn.server.service.impl;

import org.androidpn.server.dao.NotificationDao;
import org.androidpn.server.model.Notification;
import org.androidpn.server.service.NotificationService;

import java.util.List;

public class NotificationServiceImpl implements NotificationService {

	public NotificationDao notificationDao;
	
	public void saveNotification(Notification notification) {
		notificationDao.saveNotification(notification);
	}

//	public List<Notification> findNotificationsByUsername(String username) {
//		return notificationDao.findNotificationsByUsername(username);
//	}

	public void deleteNotification(Notification notification) {
		notificationDao.deleteNotification(notification);
	}

	public NotificationDao getNotificationDao() {
		return notificationDao;
	}

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void deleteNotification(String uuid) {
		notificationDao.deleteNotificationByUuid(uuid);
	}

	@Override
	public List<Notification> getNotifications() {
		return notificationDao.getNotifications();
	}

	@Override
	public Notification getNotificationByUuid(String UUID) {
		return notificationDao.getNotificationByUuid(UUID);
	}

	@Override
	public Notification getValidNotificationByUuid(String UUID) {
		return notificationDao.getValidNotificationByUuid(UUID);
	}
}
