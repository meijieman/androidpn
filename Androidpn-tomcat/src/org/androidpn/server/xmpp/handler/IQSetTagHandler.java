package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.CommonUtil;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * TODO
 * Created by MEI on 2017/11/6.
 */
public class IQSetTagHandler extends IQHandler {

    private static final String NAMESPACE = "androidpn:iq:settag";

    private SessionManager sessionManager;
    private UserService userService;

    public IQSetTagHandler() {
        userService = ServiceLocator.getUserService();
        sessionManager = sessionManager.getInstance();
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
                String username = element.elementText("username");
                String tag = element.elementText("tag");
                if (CommonUtil.isNotEmpty(username) && CommonUtil.isNotEmpty(tag))
                    sessionManager.setUserTag(username, tag);
                try {
                    User user = userService.getUser(session.getUsername());
                    user.setTag(tag);
                    userService.saveUser(user);
                } catch (UserNotFoundException | UserExistsException e) {
                    e.printStackTrace();
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
