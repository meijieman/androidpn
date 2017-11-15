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
 * 设置别名，一个名字对应一个别名，别名不能重复
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
//                if (/*CommonUtil.isNotEmpty(username) && */CommonUtil.isNotEmpty(alias)) {
                    try {
                        List<User> users = userService.getUsersByAlias(alias);
                        if (CommonUtil.isNotEmpty(users)) {
                            log.info("already exist " + users + " count bind alias " + alias);
                            // 已经有其他设备绑定了该 alias（已用业务帐号登录了），需要通知其他设备有新的绑定（当前有新设备被登录业务帐号），以让其他设备做处理（下线）
                            // TODO: 2017/11/16
//                            notificationManager.send
                        } else {
                            User user = userService.getUserByUsername(session.getUsername());
                            user.setAlias(alias);
                            userService.saveUser(user);
                            log.info("set alias " + alias + "to " + user.getUsername() + " success");
                        }
                    } catch (UserNotFoundException | UserExistsException e) {
//                        e.printStackTrace();
                        log.error("set alias error " + e);
                    }
//                }
            }
        }
        return null;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

}
