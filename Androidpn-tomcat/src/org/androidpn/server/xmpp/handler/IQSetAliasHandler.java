package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import java.util.List;

/**
 * 别名用于绑定设备号和业务帐号
 * <p>
 * 对于一个 App 而言（包名）
 * 一个设备号只能对应一个别名
 * 一个别名可以对应多个设备号（多终端）
 * <p>
 * （如果一个设备上存在多个推送 sdk，则会出现一个设备号对应多个别名，但这个别名属于不同的 APP（包名）
 * <p>
 * 1. 当只允许一个终端登录业务帐号时，推送服务端在设置别名时需要判断是否已存在与该别名绑定的设备，
 * 存在则需要推送消息到之前登录的客户端，让其主动退出登录，并告知业务服务端清除之前登录的 token
 * 2. 当允许多终端登录业务帐号时，，推送服务端在设置别名时需要判断是否已存在与该别名绑定的设备，
 * 存在则需要推送消息到之前登录的客户端，让其提示其他终端登录
 */
public class IQSetAliasHandler extends IQHandler {

    private static final String NAMESPACE = "androidpn:iq:setalias";

    private SessionManager sessionManager;
    private UserService userService;

    protected NotificationManager notificationManager;

    public IQSetAliasHandler() {
        userService = ServiceLocator.getUserService();
        sessionManager = SessionManager.getInstance();
        notificationManager = new NotificationManager();
    }

    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException {
        IQ reply = null;

        ClientSession session = sessionManager.getSession(packet.getFrom());
        if (session == null) {
            log.error("Session not found for key " + packet.getFrom());
            reply = IQ.createResultIQ(packet);
            reply.setChildElement(packet.getChildElement().createCopy());
            reply.setError(PacketError.Condition.internal_server_error);

            return reply;
        }
        if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
            if (IQ.Type.set.equals(packet.getType())) {
                Element element = packet.getChildElement();
//                String username = element.elementText("username");
                String alias = element.elementText("alias");
                User user = null;
                try {
                    user = userService.getUserByUsername(session.getUsername());
                    if (CommonUtil.isNotEmpty(user.getAlias()) && user.getAlias().equalsIgnoreCase(alias)) {
                        log.warn("already set alias " + alias + "to " + user.getUsername());
                    } else {
                        user.setAlias(alias);
                        userService.saveUser(user);
                        log.info("set alias " + alias + "to " + user.getUsername() + " success");
                    }
                    // 给当前用户发送一个设置成功穿透消息
                    String message = "用户" + user.getUsername() + "设置别名" + alias + "成功";
                    notificationManager.sendNotifcationToUser("", user.getUsername(), "", message, "", 0, "[username]" + user.getUsername(), "payload", true);

                    // alias 为空表示取消设置 alias（业务帐号退出登录），不需要发送消息
                    if (CommonUtil.isNotEmpty(alias)) {
                        List<User> users = userService.getUsersByAlias(alias);
                        if (CommonUtil.isNotEmpty(users)) {
                            log.info("already exist " + users + " count bind alias " + alias);
                            // 已经有其他设备绑定了该 alias（已用业务帐号登录了），需要通知其他设备有新的绑定（当前有新设备被登录业务帐号），以让其他设备做处理（下线，提示新设备登录）
                            String message1 = "新增用户" + user.getUsername() + "设置别名" + alias + "，当前用户需要提示或下线";
                            for (User user1 : users) {
                                // 排除当前 alias
                                if (!user.getUsername().equalsIgnoreCase(user1.getUsername())) {
                                    notificationManager.sendNotifcationToUser("", user1.getUsername(), "", message1, "", 0, "[username]" + user1.getUsername(), "payload", true);
                                }
                            }
                        }
                    }
                } catch (UserNotFoundException | UserExistsException e) {
                    log.error("设置 alias 失败 " + e);
                    // 给当前用户发送一个设置失败穿透消息
                    if (user != null) {
                        String message = "用户" + user.getUsername() + "设置别名" + alias + "失败";
                        notificationManager.sendNotifcationToUser("", user.getUsername(), "", message, "", 0, "[username]", "payload", true);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

}
