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

import org.androidpn.server.model.OperationLog;
import org.androidpn.server.service.OperationLogService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.util.Config;
import org.androidpn.server.util.DateUtil;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/** 
 * A controller class to process the notification related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;
    private OperationLogService operationLogService;

    public NotificationController() {
        notificationManager = new NotificationManager();
        operationLogService = ServiceLocator.getOperationLogService();
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        // 设置已运行时间
        List<OperationLog> operationLogs = operationLogService.getOperationLogs();
        String interval = "已运行: ";
        if (!operationLogs.isEmpty()) {
            OperationLog operationLog = operationLogs.get(operationLogs.size() - 1);
            String start = DateUtil.format(operationLog.getCreatedDate());
            String end = DateUtil.format(operationLog.getUpdateDate());
            interval += DateUtil.dateDiff(start, end, DateUtil.YYYY_MM_DD_HH_MM_SS);
        }
        mav.addObject("operation_time", interval);
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
        String validTime = ServletRequestUtils.getStringParameter(request, "validTime", "0");

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);

        String pushTypeParam = "";
        long validTimeParam = 0L;
        if ("0".equalsIgnoreCase(pushType)) {
            pushTypeParam = "notification";
        } else if("1".equalsIgnoreCase(pushType)){
            pushTypeParam = "payload";
        }
        try {
            validTimeParam = Long.parseLong(validTime);
        } catch (NumberFormatException e) {
//            e.printStackTrace();
            logger.error("the param validTime should be number " + validTime, e);
        }

        if (broadcast.equalsIgnoreCase("0")) { // broadcast
            notificationManager.sendBroadcast(apiKey, title, message, uri, validTimeParam, "[broadcast]", pushTypeParam);
        } else if (broadcast.equalsIgnoreCase("1")) { // by username
            notificationManager.sendNotifcationToUser(apiKey, username, title, message, uri, validTimeParam, "[username]" + username, pushTypeParam, true);
        } else if (broadcast.equalsIgnoreCase("2")) {
            // 别名推送
            notificationManager.sendNotificationByAlias(alias, apiKey, title, message, uri, validTimeParam, "[alias]" + alias + username, pushTypeParam, true);
        } else if (broadcast.equalsIgnoreCase("3")) {
            // 标签推送
            if (CommonUtil.isNotEmpty(tags)) {
                String[] split = tags.split(",");
                notificationManager.sendNotificationByTags(split[0], apiKey, title, message, uri, validTimeParam, "[tags]" + tags, pushTypeParam, true);
            }
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do"); // 重定向
        return mav;
    }

}
