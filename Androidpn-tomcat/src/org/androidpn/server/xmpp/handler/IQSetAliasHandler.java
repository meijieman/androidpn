package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * 设置别名，一个名字对应一个别名，别名不能重复
 */
public class IQSetAliasHandler extends IQHandler {

    private static final String NAMESPACE = "androidpn:iq:setalias";

    private SessionManager sessionManager;
    private UserService userService;

    public IQSetAliasHandler() {
        userService = ServiceLocator.getUserService();
        sessionManager = SessionManager.getInstance();
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
                        if (userService.existAlias(alias)) {
                            log.warn("alias " + alias + "has been used");
                        } else {
                            User user = userService.getUserByUsername(session.getUsername());
                            user.setAlias(alias);
                            userService.saveUser(user);
                            log.info("set alias " + alias + "to " + user.getUsername() + " success");
                        }
                    } catch (UserNotFoundException | UserExistsException e) {
                        e.printStackTrace();
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
