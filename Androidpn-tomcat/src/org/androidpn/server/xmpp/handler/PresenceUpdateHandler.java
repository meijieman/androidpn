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
package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.Notification;
import org.androidpn.server.model.PushDetail;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.PushDetailService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.androidpn.server.xmpp.router.PacketDeliverer;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.Presence;

import java.util.Date;
import java.util.List;


/** 
 * This class is to handle the presence protocol.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PresenceUpdateHandler {

    protected final Log log = LogFactory.getLog(getClass());

    protected SessionManager sessionManager;
    
    protected NotificationService notificationService;
    protected PushDetailService mPushDetailService;
    
    protected NotificationManager notificationManager;

    /**
     * Constructor.
     */
    public PresenceUpdateHandler() {
        sessionManager = SessionManager.getInstance();
        notificationService = ServiceLocator.getNotificationService();
        mPushDetailService = ServiceLocator.getPushDetailService();
        notificationManager = new NotificationManager();
    }

    /**
     * Processes the presence packet.
     * 
     * @param packet the packet
     */
    public void process(Packet packet) {
        log.info("process() 用户状态更新");
        ClientSession session = sessionManager.getSession(packet.getFrom());

        try {
            Presence presence = (Presence) packet;
            Presence.Type type = presence.getType();

            if (type == null) { // null == available
                if (session != null
                        && session.getStatus() == Session.STATUS_CLOSED) {
                    log.warn("Rejected available presence: " + presence + " - "
                            + session);
                    return;
                }

                if (session != null) {
                    session.setPresence(presence);
                    if (!session.isInitialized()) {
                        // initSession(session);
                        session.setInitialized(true);
                    }
                    //发送离线消息给用户
                    List<PushDetail> details = mPushDetailService.getPushDetailsThatFailureByUsername(session.getUsername());
                    log.info("process() 发送离线消息给用户 " + details);
                    if (CommonUtil.isNotEmpty(details)) {
                        for (PushDetail detail : details) {
                            // 更新发送时间
                            Notification notification = notificationService.getValidNotificationByUuid(detail.getUuid());
                            if (notification != null) {
                                Date date = new Date();
                                log.info("tag_valid" + notification.getCreatedDate().getTime());
                                log.info("tag_valid now " + date.getTime());
                                if (notification.getValidTime() == 0L
                                        || date.getTime() - notification.getCreatedDate().getTime() < notification.getValidTime() * 1000) {
                                    // 消息没有过期
                                    detail.setDeliveredDate(date);
                                    mPushDetailService.savePushDetail(detail);

                                    // 只需要发送通知，不需要保存通知记录（之前已经保存过）
                                    notificationManager.sendNotifcationToUser(session.getUsername(), notification, false);
                                } else {
                                    // 更新消息为过期状态
                                    notification.setState(Notification.STATE_EXPIRED);
                                    notificationService.saveNotification(notification);
                                }
                            } else {
                                log.warn("uuid " + detail.getUuid() + " has no valid notification that not push");
                            }
                        }
                    }
                }

            } else if (Presence.Type.unavailable == type) {

                if (session != null) {
                    session.setPresence(presence);
                }

            } else {
                presence = presence.createCopy();
                if (session != null) {
                    presence.setFrom(new JID(null, session.getServerName(),
                            null, true));
                    presence.setTo(session.getAddress());
                } else {
                    JID sender = presence.getFrom();
                    presence.setFrom(presence.getTo());
                    presence.setTo(sender);
                }
                presence.setError(PacketError.Condition.bad_request);
                PacketDeliverer.deliver(presence);
            }

        } catch (Exception e) {
            log.error("Internal server error. Triggered by packet: " + packet,
                    e);
        }
    }

}
