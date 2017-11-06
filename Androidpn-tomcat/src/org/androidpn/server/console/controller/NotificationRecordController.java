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
package org.androidpn.server.console.controller;

import org.androidpn.server.model.Notification;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.ServiceLocator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查看所有推送消息
 */
public class NotificationRecordController extends MultiActionController {


    private NotificationService mNotificationService;

    public NotificationRecordController() {
        mNotificationService = ServiceLocator.getNotificationService();
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Notification> notifications = mNotificationService.getNotifications();
        ModelAndView mav = new ModelAndView();
        mav.addObject("notificationList", notifications);
        mav.setViewName("notificationrecord/list");
        return mav;
    }
}
