package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.PushDetail;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.PushDetailService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import java.util.Date;


public class IQDeliverConfirmHandler extends IQHandler {

    private static final String NAMESPACE = "androidpn:iq:deliverconfirm";

    private NotificationService notificationService;
    private PushDetailService mPushDetailService;

    public IQDeliverConfirmHandler() {
        notificationService = ServiceLocator.getNotificationService();
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
//        		notificationService.deleteNotification(uuid);
                // FIXME: 2017/11/7 根据用户名和消息 uuid 更新消息状态, uuid 有问题，username 没有
                PushDetail pushDetail = mPushDetailService.getPushDetail("9744f50237f54c37808736ff37808a78", uuid);
                if (pushDetail != null) {
                    pushDetail.setReceiptDate(new Date());
                    mPushDetailService.savePushDetail(pushDetail);
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
