/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.xmpp.push;

import org.androidpn.server.model.Notification;
import org.androidpn.server.model.PushDetail;
import org.androidpn.server.model.User;
import org.androidpn.server.service.*;
import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This class is to manage sending the notifcations to the users.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationManager {

    private static final String NOTIFICATION_NAMESPACE = "androidpn:iq:notification";

    private final Log log = LogFactory.getLog(getClass());

    private SessionManager sessionManager;

    private NotificationService notificationService;

    private UserService userService;

    private PushDetailService mPushDetailService;

    /**
     * Constructor.
     */
    public NotificationManager() {
        sessionManager = SessionManager.getInstance();
        notificationService = ServiceLocator.getNotificationService();
        userService = ServiceLocator.getUserService();
        mPushDetailService = ServiceLocator.getPushDetailService();
    }

    /**
     * Broadcasts a newly created notification message to all connected users.
     *
     * @param apiKey  the API key
     * @param title   the title
     * @param message the message details
     * @param uri     the uri
     */
    public void sendBroadcast(String apiKey, String title, String message, String uri) {
        log.debug("sendBroadcast()...");
        List<User> users = userService.getUsers();
        for (User user : users) {
            Random random = new Random();
            String id = Integer.toHexString(random.nextInt());
            IQ notificationIQ = createNotificationIQ(id, apiKey, title, message, uri);
            ClientSession session = sessionManager.getSession(user.getUsername());
            if (session != null && session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
            saveNotification(apiKey, user.getUsername(), title, message, uri, id);
        }
    }

    public void sendBroadcast(String apiKey, String title, String message, String uri, String pushTo, String pushType) {
        log.debug("sendBroadcast()...");
        if (CommonUtil.isEmpty(userService.getUsers())) {
            log.debug("sendBroadcast() no user, finish");
            return;
        }

        Notification notif = createNotification(apiKey, title, message, uri, pushTo, pushType);
        notificationService.saveNotification(notif);

        PushDetail pd;

        log.info("tag_pd 将要发送推送 ");
        List<User> users = userService.getUsers();
        for (User user : users) {
            pd = new PushDetail();
            pd.setUuid(notif.getUuid());
            pd.setUsername(user.getUsername());
            Date date = new Date();
            pd.setCreatedDate(date); // fixme 每次都 new 时间
            pd.setDeliveredDate(date);
            PushDetail pd1 = mPushDetailService.savePushDetail(pd);
            log.info("tag_pd 保存推送 pd " + pd1);

            IQ notificationIQ = createNotificationIQ(notif);
            ClientSession session = sessionManager.getSession(user.getUsername());
            if (session != null && session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
        }
    }

    private Notification createNotification(String apiKey, String title, String message, String uri,
                                            String pushTo, String pushType){
        Notification notif = new Notification();
        notif.setApiKey(apiKey);
        notif.setTitle(title);
        notif.setMessage(message);
        notif.setUri(uri);
        notif.setPushTo(pushTo);
        notif.setPushType(pushType);

        Random random = new Random();
        String uuid = Integer.toHexString(random.nextInt());
        notif.setUuid(uuid);
        notif.setCreatedDate(new Date());
        return notif;
    }

    // 为在线用户推送消息，离线用户不推送
    public void sendBroadcast2Presence(String apkKey, String title, String message, String uri) {
        log.debug("sendBroadcast() 2 presence... ");
        for (ClientSession session : sessionManager.getSessions()) {
            String id = Integer.toHexString(new Random().nextInt());
            IQ notificationIQ = createNotificationIQ(id, apkKey, title, message, uri);
            if (session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
        }
    }

    /**
     * Sends a newly created notification message to the specific user.
     *
     * @param apiKey  the API key
     * @param title   the title
     * @param message the message details
     * @param uri     the uri
     */
    public void sendNotifcationToUser(String apiKey, String username, String title, String message,
                                      String uri, boolean shouldSave) {
        log.debug("sendNotifcationToUser()...");
        Random random = new Random();
        String id = Integer.toHexString(random.nextInt());
        IQ notificationIQ = createNotificationIQ(id, apiKey, title, message, uri); // 创建推送
        ClientSession session = sessionManager.getSession(username);
        if (session != null) {
            if (session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ); // 发送推送
            }
        }
        try {
            User user = userService.getUserByUsername(username);
            if (user != null && shouldSave) {
                saveNotification(apiKey, username, title, message, uri, id);
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendNotifcationToUser(String apiKey, String username, String title, String message, String uri,
                                      String pushTo, String pushType, boolean shouldSave) {
        log.debug("sendNotifcationToUser()...");
        Notification notif = createNotification(apiKey, title, message, uri, pushTo, pushType);

        IQ notificationIQ = createNotificationIQ(notif); // 创建推送
        ClientSession session = sessionManager.getSession(username);
        if (session != null) {
            if (session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ); // 发送推送
            }
        }
        try {
            User user = userService.getUserByUsername(username);
            if (user != null && shouldSave) {
                notificationService.saveNotification(notif);

                PushDetail pd = new PushDetail();
                pd.setUuid(notif.getUuid());
                pd.setUsername(user.getUsername());
                Date date = new Date();
                pd.setCreatedDate(date);
                pd.setDeliveredDate(date);
                mPushDetailService.savePushDetail(pd);
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendNotifcationToUser(String username, Notification notif, boolean shouldSave) {
        log.debug("sendNotifcationToUser()...");

        IQ notificationIQ = createNotificationIQ(notif); // 创建推送
        ClientSession session = sessionManager.getSession(username);
        if (session != null) {
            if (session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ); // 发送推送
            }
        }
        try {
            User user = userService.getUserByUsername(username);
            if (user != null && shouldSave) {
                notificationService.saveNotification(notif);

                PushDetail pd = new PushDetail();
                pd.setUuid(notif.getUuid());
                pd.setUsername(user.getUsername());
                Date date = new Date();
                pd.setCreatedDate(date);
                pd.setDeliveredDate(date);
                mPushDetailService.savePushDetail(pd);
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    //通过别名发送通知
    public void sendNotificationByAlias(String alias, String apiKey, String title, String message,
                                        String uri, String pushTo, String pushType, boolean shouldsave) {
        try {
            User user = userService.getUserByAlias(alias);
            Notification notif = createNotification(apiKey, title, message, uri, pushTo, pushType);
            sendNotifcationToUser(user.getUsername(), notif, shouldsave);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 通过标签发送
    public void sendNotificationByTags(String tag, String apiKey, String title, String message,
                                      String uri, String pushTo, String pushType, boolean shouldsave) {
        log.debug("sendNotificationByTags()...");
        List<User> users = userService.getUsersByTag(tag);
        if (CommonUtil.isEmpty(users)) {
            log.debug("sendNotificationByTags() no user, finish");
            return;
        }

        Notification notif = createNotification(apiKey, title, message, uri, pushTo, pushType);
        notificationService.saveNotification(notif);

        PushDetail pd;
        log.info("tag_pd 将要发送推送 ");

        for (User user : users) {
            pd = new PushDetail();
            pd.setUuid(notif.getUuid());
            pd.setUsername(user.getUsername());
            Date date = new Date();
            pd.setCreatedDate(date);// fixme 每次都 new 时间
            pd.setDeliveredDate(date);
            PushDetail pd1 = mPushDetailService.savePushDetail(pd);
            log.info("tag_pd 保存推送 pd " + pd1);

            IQ notificationIQ = createNotificationIQ(notif);
            ClientSession session = sessionManager.getSession(user.getUsername());
            if (session != null && session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
        }
    }

    //保存离线消息
    public void saveNotification(String apiKey, String username,
                                 String title, String message, String uri, String uuid) {
        Notification notification = new Notification();
        notification.setApiKey(apiKey);
        notification.setUri(uri);
        notification.setMessage(message);
        notification.setTitle(title);
        notification.setUuid(uuid);
        notification.setCreatedDate(new Date());
        notificationService.saveNotification(notification);
    }

    /**
     * Creates a new notification IQ and returns it.
     */
    private IQ createNotificationIQ(String id, String apiKey, String title,
                                    String message, String uri) {
        // String id = String.valueOf(System.currentTimeMillis());

        Element notification = DocumentHelper.createElement(QName.get(
                "notification", NOTIFICATION_NAMESPACE));
        notification.addElement("id").setText(id);
        notification.addElement("apiKey").setText(apiKey);
        notification.addElement("title").setText(title);
        notification.addElement("message").setText(message);
        notification.addElement("uri").setText(uri);

        IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(notification);

        return iq;
    }

    private IQ createNotificationIQ(Notification notif) {
        // String id = String.valueOf(System.currentTimeMillis());

        Element notification = DocumentHelper.createElement(QName.get("notification",
                NOTIFICATION_NAMESPACE));
        notification.addElement("id").setText(String.valueOf(notif.getId()));
        notification.addElement("uuid").setText(String.valueOf(notif.getUuid()));
        notification.addElement("apiKey").setText(notif.getApiKey());
        notification.addElement("title").setText(notif.getTitle());
        notification.addElement("message").setText(notif.getMessage());
        notification.addElement("uri").setText(notif.getUri());
        notification.addElement("pushType").setText(notif.getPushType());
        notification.addElement("createdDate").setText(notif.getCreatedDate().toString());

        IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(notification);

        return iq;
    }
}
