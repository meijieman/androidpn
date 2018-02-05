# AndroidPN

## 原始项目地址
https://github.com/dannytiehui/androidpn

## 二次开发项目地址
https://github.com/xuyusong/AndroidPN

二次开发项目已实现功能
加入断线重连、心跳包、离线推送、标签推送。

## 新增功能
1. 标签推送需要增加离线推送 
已实现
2. 支持正则匹配推送
标签推送多个情况
A&B, A|B, ^A

3. 压力测试，稳定性测试，记录测试日志，重连测试，客户端耗电量测试

4. 富文本推送

5. 心跳机制，需要改为配置文件，获取最佳心跳间隔

6. 服务端增加推送成功记录
已实现
7. 限制推送频率，

8. 客户端增加消息提示机制，如打开 app 时才提示，屏幕点亮时才提示，wifi状态时才提示

9. 穿透消息
已实现
10. 统计客户端查看状态
部分实现
11. 客户端开机启动
已实现
12. 一个客户端，卸载安装后 id 不变
需要修改 服务端 UserServiceImpl.saveUser(User) 保存时添加是否已存在的判断
客户端 XmppManager.RegisterTask#run 注册时生成用户名和密码的逻辑


13. 客户端服务保活

14. 多个客户端共存
目前能够多个客户端，但是每个客户端的 service 是独立的

15. 客户端网络连接的策略

16. 服务端权限管理

17. 客户端给服务端发送消息，且有回调
客户端可以给服务端发送别名，标签消息
已实现

18. 消息推送有效期
已实现

## buglist
1. 当服务端连续发送多条通知时，客户端都是显示同一条通知内容。
已修改
2. 服务端需要先保存消息，再发送到客户端。防止客户端返回消息回执比客户端保存消息早
已修改
3. 不能在非主进程中启动服务

4. 推送内容不能有表情


bug 解决方案
http://www.xuehuile.com/blog/f10be7d09aa848b0889761613e2edd8e.html
http://topmanopensource.iteye.com/blog/1609214
http://lpffan.iteye.com/blog/1456546
https://zhuanlan.zhihu.com/p/22909985


参考文档
* http://blog.csdn.net/banketree/article/details/8852046
* https://zhuanlan.zhihu.com/c_51051095
* Android推送技术研究
https://www.jianshu.com/p/584707554ed7
* Android微信智能心跳方案
https://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=207243549&idx=1&sn=4ebe4beb8123f1b5ab58810ac8bc5994



总结一下以 androidpn 为典型的开源 Android Push 方案会存在的问题：
１）容量大了开源服务器实现顶不住，还是需要自己去改进开源实现，或者完全重新用新方案，开发投入与高成本是不可避免的。
２）协议与实现上如流量消耗、网络连接策略等，不是专门为移动 Push 优化过的，是不经济的。

====================================

应用被杀死

10-08 21:38:09.580 1109-1109/? I/TMonitorManager: org.androidpn.demoapp
10-08 21:38:14.650 940-951/? I/ActivityManager: Force stopping org.androidpn.demoapp appid=10129 user=0: from pid 1336
10-08 21:38:14.650 940-951/? I/ActivityManager: Killing 19010:org.androidpn.demoapp/u0a129 (adj 15): stop org.androidpn.demoappfrom pid 1336
10-08 21:38:14.650 940-951/? W/ActivityManager: Scheduling restart of crashed service org.androidpn.demoapp/org.androidpn.client.NotificationService in 1000ms
10-08 21:38:14.680 940-951/? I/ActivityManager:   Force stopping service ServiceRecord{421d96c8 u0 org.androidpn.demoapp/org.androidpn.client.NotificationService}

----------

MyEclipse 需要使用 jdk7的，使用8的有问题

----------

idea
问题：成功运行 tomcat 但没有初始化 spring
解决：Projcet Structure -> Modules -> Web -> Source Roots 需要勾选 src 和 resources

java.lang.RuntimeException: Configuration loading error: config.xml
Projcet Structure -> Modules -> Web -> Source Roots 需要勾选 resources

---------

问题：java.lang.RuntimeException: Configuration loading error: config.xml

解决办法：由于当前项目 resources/ 与 src/ 同一级别，导致生成 artifacts 时没有将 resources/ 编译，需要在 Projcet Structure ->aritfacts -> WEB-INF -> classes -> 点击添加，添加 Directory Contents -> 指向 resources/

resource(s)目录也是classpath，和src目录一样，里面的文件都会放在classes目录下面

---------
idea 打包 war

Build -> build artifacts... -> edit... -> Type 选择 Web Application:Archive -> OK
Build -> build artifacts... -> Rebuild -- 之后会默认在 out/artifacts/androidpn_war_exploded 生成 war 包

---------
C:\Users\Administrator>mysql -u root -p
Enter password: ****
ERROR 2003 (HY000): Can't connect to MySQL server on 'localhost' (10061)

未启动 mysql 服务


解决方法：进入mysql的安装目录下的bin目录，打开DOS命令窗口，输入命令：mysqld --install，提示安装服务成功。
如果要卸载服务，可以输入如下命令：mysqld --remove

---------
错误

org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.springframework.jdbc.UncategorizedSQLException: Hibernate operation: could not insert: [org.androidpn.server.model.Notification]; uncategorized SQLException for SQL [/* insert org.androidpn.server.model.Notification */ insert into apn_notification (api_key, message, title, uri, user_name, uuid) values (?, ?, ?, ?, ?, ?)]; SQL state [HY000]; error code [1366]; Incorrect string value: '\xE6\xB6\x88\xE6\x81\xAF...' for column 'message' at row 1; nested exception is java.sql.SQLException: Incorrect string value: '\xE6\xB6\x88\xE6\x81\xAF...' for column 'message' at row 1

mysql 字符集错误，应为 utf8


----------

