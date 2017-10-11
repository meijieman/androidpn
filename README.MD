

## 原项目地址
https://github.com/xuyusong/AndroidPN

原项目已实现功能

基于Androidpn的推送服务器方案，加入断线重连、心跳包、离线推送、标签推送。

https://github.com/dannytiehui/androidpn


## 需要新增
1. 标签推送需要增加离线推送
2. 支持正则匹配推送
3. 压力测试，稳定性测试，记录测试日志，重连测试，客户端耗电量测试
4. 富文本推送
5. 心跳机制，需要改为配置文件，获取最佳心跳间隔
6. 服务端增加推送成功记录
7. 限制推送频率，离线消息保存时长
8. 客户端增加消息提示机制，如打开 app 时才提示，屏幕点亮时才提示，wifi状态时才提示
9. 穿透消息
10. 统计客户端查看状态
11. 客户端开机启动
12. 一个客户端，卸载安装后 id 不变

需要修改 服务端 UserServiceImpl.saveUser(User) 保存时添加是否已存在的判断
客户端 XmppManager.RegisterTask#run 注册时生成用户名和密码的逻辑

13. 客户端服务保活
14. 能不能多个客户端共存
15. 客户端网络连接的策略
16. 服务端权限管理


## buglist
1. 当服务端连续发送多条通知时，客户端都是显示同一条通知内容。
2. 服务端需要先保存消息，再发送到客户端。防止客户端返回消息回执比客户端保存消息早
3. 解决服务器端重启后,客户端不能成功连接androidpn服务器
	XmppManager#addTask 添加 runTask()


bug 解决方案
http://www.xuehuile.com/blog/f10be7d09aa848b0889761613e2edd8e.html
http://topmanopensource.iteye.com/blog/1609214
http://lpffan.iteye.com/blog/1456546
https://zhuanlan.zhihu.com/p/22909985


参考文档
http://blog.csdn.net/banketree/article/details/8852046
	

总结一下以 androidpn 为典型的开源 Android Push 方案会存在的问题：
１）容量大了开源服务器实现顶不住，还是需要自己去改进开源实现，或者完全重新用新方案，开发投入与高成本是不可避免的。
２）协议与实现上如流量消耗、网络连接策略等，不是专门为移动 Push 优化过的，是不经济的。


应用被杀死

10-08 21:38:09.580 1109-1109/? I/TMonitorManager: org.androidpn.demoapp
10-08 21:38:14.650 940-951/? I/ActivityManager: Force stopping org.androidpn.demoapp appid=10129 user=0: from pid 1336
10-08 21:38:14.650 940-951/? I/ActivityManager: Killing 19010:org.androidpn.demoapp/u0a129 (adj 15): stop org.androidpn.demoappfrom pid 1336
10-08 21:38:14.650 940-951/? W/ActivityManager: Scheduling restart of crashed service org.androidpn.demoapp/org.androidpn.client.NotificationService in 1000ms
10-08 21:38:14.680 940-951/? I/ActivityManager:   Force stopping service ServiceRecord{421d96c8 u0 org.androidpn.demoapp/org.androidpn.client.NotificationService}


====================================


极光推送
个推


在mima中已经实现了很多事情, 
比如： 
使用LoggingFilter记录发送的数据内容。 
使用ProtocolCodecFilter进行二进制内容或者POJO的对象传输。 
使用CompressionFilter对数据内容进行压缩。 
用SSLFilter对传输数据进行加密。 




XMPP协议


Iq是(Info/Query)的缩写, 是三种通讯原语之一，其他两个为message和presence. 
该原语应用于”请求-应答”机制。 




MyEclipse 需要使用 jdk7的，使用8的有问题