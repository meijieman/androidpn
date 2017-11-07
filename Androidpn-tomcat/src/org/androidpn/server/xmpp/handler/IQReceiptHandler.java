package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.PushDetail;
import org.androidpn.server.service.PushDetailService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import java.util.Date;


public class IQReceiptHandler extends IQHandler {

    private static final String NAMESPACE = "androidpn:iq:receipt";

    private PushDetailService mPushDetailService;

    public IQReceiptHandler() {
        mPushDetailService = ServiceLocator.getPushDetailService();
    }

    @Override
    public IQ handleIQ(IQ packet) throws UnauthorizedException {
        IQ reply = null;
        log.debug("tag_route 处理回执消息");
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
                String uuid = element.elementText("uuid");
                try {
                    // 根据用户名和消息的 uuid 更新消息状态（即回执时间）
                    PushDetail pushDetail = mPushDetailService.getPushDetail(session.getUsername(), uuid);
                    if (pushDetail != null) {
                        pushDetail.setReceiptDate(new Date());
                        mPushDetailService.savePushDetail(pushDetail);
                    }
                } catch (UserNotFoundException e) {
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
