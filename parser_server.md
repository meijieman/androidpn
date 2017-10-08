NotificationController 在 /Androidpn-tomcat/WebRoot/WEB-INF/dispatcher-servlet.xml 中配置

NotificationController.send(HttpServletRequest, HttpServletResponse)

NotificationManager.sendBroadcast(String, String, String, String)


会话管理
/Androidpn-tomcat/resources/spring-config.xml 中 配置了 XmppIoHandler

XmppIoHandler负责处理了与客户端的交互部分

XmppIoHandler 用来处理MINA触发的I/O事件

XmppIoHandler.sessionOpened(IoSession)

XmppIoHandler.messageReceived(IoSession, Object)

StanzaHandler.process(String, XMPPPacketReader) 分发事件 登录，消息处理，iq


入口类
org.androidpn.server.xmpp.XmppServer





服务端启动流程
/Androidpn-tomcat/WebRoot/WEB-INF/dispatcher-servlet.xml
NotificationApiController
NotificationManager
XmppServer
/Androidpn-tomcat/resources/spring-config.xml
ioAcceptor 初始化 MINA 框架
mina -> XmppIoHandler -> StanzaHandler -> PacketRouter -> IQHandler -> IQAuthHandler,IQRegisterHandler, IQRosterHandler






NotificationApiController.send(HttpServletRequest, HttpServletResponse)

messageSent事件
当消息已被远端接收到的时候，会触发messageSent事件



