NotificationController �� /Androidpn-tomcat/WebRoot/WEB-INF/dispatcher-servlet.xml ������

NotificationController.send(HttpServletRequest, HttpServletResponse)

NotificationManager.sendBroadcast(String, String, String, String)


�Ự����
/Androidpn-tomcat/resources/spring-config.xml �� ������ XmppIoHandler

XmppIoHandler����������ͻ��˵Ľ�������

XmppIoHandler ��������MINA������I/O�¼�

XmppIoHandler.sessionOpened(IoSession)

XmppIoHandler.messageReceived(IoSession, Object)

StanzaHandler.process(String, XMPPPacketReader) �ַ��¼� ��¼����Ϣ����iq


�����
org.androidpn.server.xmpp.XmppServer





�������������
/Androidpn-tomcat/WebRoot/WEB-INF/dispatcher-servlet.xml
NotificationApiController
NotificationManager
XmppServer
/Androidpn-tomcat/resources/spring-config.xml
ioAcceptor ��ʼ�� MINA ���
mina -> XmppIoHandler -> StanzaHandler -> PacketRouter -> IQHandler -> IQAuthHandler,IQRegisterHandler, IQRosterHandler






NotificationApiController.send(HttpServletRequest, HttpServletResponse)

messageSent�¼�
����Ϣ�ѱ�Զ�˽��յ���ʱ�򣬻ᴥ��messageSent�¼�



