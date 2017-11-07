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

import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * A controller class to process the notification related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;

    public NotificationController() {
        notificationManager = new NotificationManager();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        mav.setViewName("notification/form");
        return mav;
    }

    public ModelAndView send(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 发送推送消息
        String broadcast = ServletRequestUtils.getStringParameter(request, "broadcast", "0");
        String username = ServletRequestUtils.getStringParameter(request, "username");
        String alias = ServletRequestUtils.getStringParameter(request, "alias");
        String tags = ServletRequestUtils.getStringParameter(request, "tags");
        String title = ServletRequestUtils.getStringParameter(request, "title");
        String message = ServletRequestUtils.getStringParameter(request, "message");
        String uri = ServletRequestUtils.getStringParameter(request, "uri");
        String pushType = ServletRequestUtils.getStringParameter(request, "pushtype", "0");

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);

        String pushTypeParam = "";
        if ("0".equalsIgnoreCase(pushType)) {
            pushTypeParam = "notification";
        } else if("1".equalsIgnoreCase(pushType)){
            pushTypeParam = "payload";
        }

        if (broadcast.equalsIgnoreCase("0")) { // broadcast
            notificationManager.sendBroadcast(apiKey, title, message, uri, "[broadcast]", pushTypeParam);
//            notificationManager.sendBroadcast(apiKey, title, message, uri);
        } else if (broadcast.equalsIgnoreCase("1")) { // by username
            notificationManager.sendNotifcationToUser(apiKey, username, title, message, uri, "[username]", pushTypeParam, true);
//            notificationManager.sendNotifcationToUser(apiKey, username, title, message, uri,true);
        } else if (broadcast.equalsIgnoreCase("2")) {
            //别名推送
            notificationManager.sendNotificationByAlias(alias, apiKey, title, message, uri, "[alias]" + username, pushTypeParam, true);
//        	notificationManager.sendNotificationByAlias(alias, apiKey, title, message, uri, false);
        } else if (broadcast.equalsIgnoreCase("3")) {
            if (CommonUtil.isNotEmpty(tags)) {
                String[] split = tags.split(",");
                notificationManager.sendNotificationByTags(split[0], apiKey, title, message, uri, "[tags]" + tags, pushTypeParam, true);
            }
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do"); // 重定向
        return mav;
    }

}
