/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hongfans.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;

import com.hongfans.push.iq.NotificationIQ;
import com.hongfans.push.iq.listener.NotificationPacketListener;
import com.hongfans.push.iq.provider.NotificationIQProvider;
import com.hongfans.push.util.DeviceUuidFactory;
import org.jivesoftware.smack.util.LogUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * This class is to manage the XMPP connection between client and server.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager{

    private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

    private NotificationService context;

    private NotificationService.TaskSubmitter taskSubmitter;

    private NotificationService.TaskTracker taskTracker;

    private SharedPreferences sharedPrefs;

    private String xmppHost;

    private int xmppPort;

    private XMPPConnection connection;

    private String username;

    private String password;

    private ConnectionListener connectionListener;

    private PacketListener notificationPacketListener;

    private Handler handler;

    private List<Runnable> taskList;

    private boolean running = false;

    private Future<?> futureTask;

    private Thread reconnection;

    public XmppManager(NotificationService notificationService){
        context = notificationService;
        taskSubmitter = notificationService.getTaskSubmitter();
        taskTracker = notificationService.getTaskTracker();
        sharedPrefs = notificationService.getSharedPreferences();

        xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
        xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
        username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
        password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");

        connectionListener = new PersistentConnectionListener(this);
        notificationPacketListener = new NotificationPacketListener(this);

        handler = new Handler();
        taskList = new ArrayList<>();
        reconnection = new ReconnectionThread(this);
    }

    public Context getContext(){
        return context;
    }

    public void connect(){
        LogUtil.d("connect()...");
        submitLoginTask();
    }

    public void disconnect(){
        LogUtil.d("disconnect()...");
        terminatePersistentConnection();
    }

    public void terminatePersistentConnection(){
        LogUtil.d("terminatePersistentConnection()...");
        Runnable runnable = new Runnable(){

            final XmppManager xmppManager = XmppManager.this;

            public void run(){
                if(xmppManager.isConnected()){
                    LogUtil.d("terminatePersistentConnection()... run()");
                    xmppManager.getConnection().removePacketListener(
                            xmppManager.getNotificationPacketListener());
                    xmppManager.getConnection().disconnect();
                }
                xmppManager.runTask();
            }
        };
        addTask(runnable);
    }

    public XMPPConnection getConnection(){
        return connection;
    }

    public void setConnection(XMPPConnection connection){
        this.connection = connection;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public ConnectionListener getConnectionListener(){
        return connectionListener;
    }

    public PacketListener getNotificationPacketListener(){
        return notificationPacketListener;
    }

    /**
     * 断线重连
     */
    public void startReconnectionThread(){
        synchronized(reconnection){
            if(reconnection == null || !reconnection.isAlive()){
                reconnection = new ReconnectionThread(this); // 新创建一个线程
                reconnection.setName("Xmpp Reconnection Thread");
                reconnection.start();
            }
        }
    }

    public Handler getHandler(){
        return handler;
    }

    public void reregisterAccount(){
        removeAccount();
        submitLoginTask();
        runTask();
    }

    public List<Runnable> getTaskList(){
        return taskList;
    }

    public Future<?> getFutureTask(){
        return futureTask;
    }

    public void runTask(){
        LogUtil.d("runTask()...");
        synchronized(taskList){
            running = false;
            futureTask = null;
            if(!taskList.isEmpty()){
                Runnable runnable = taskList.get(0);
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if(futureTask == null){
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        LogUtil.d("runTask()...done");
    }

    private String newRandomUUID(){
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

    private boolean isConnected(){
        return connection != null && connection.isConnected();
    }

    public boolean isAuthenticated(){
        return connection != null && connection.isConnected()
               && connection.isAuthenticated();
    }

    private boolean isRegistered(){
        return sharedPrefs.contains(Constants.XMPP_USERNAME)
               && sharedPrefs.contains(Constants.XMPP_PASSWORD);
    }

    private void submitConnectTask(){
        LogUtil.d("submitConnectTask()...");
        addTask(new ConnectTask());
    }

    private void submitRegisterTask(){
        LogUtil.d("submitRegisterTask()...");
        submitConnectTask();
        addTask(new RegisterTask());
    }

    private void submitLoginTask(){
        LogUtil.d("submitLoginTask()...");
        submitRegisterTask();
        addTask(new LoginTask());
    }

    private void addTask(Runnable runnable){
        LogUtil.d("addTask(runnable)...");
        taskTracker.increase(); // 计数器
        synchronized(taskList){
            if(taskList.isEmpty() && !running){
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if(futureTask == null){
                    taskTracker.decrease();
                }
            } else {
                taskList.add(runnable);
            }
        }
        LogUtil.d("addTask(runnable)... done");
    }

    private void removeAccount(){
        Editor editor = sharedPrefs.edit();
        editor.remove(Constants.XMPP_USERNAME);
        editor.remove(Constants.XMPP_PASSWORD);
        editor.commit();
    }

    //删除任务队列
    private void dropTask(int dropCount){
        synchronized(taskList){
            if(taskList.size() >= dropCount){
                for(int i = 0; i < dropCount; i++){
                    taskList.remove(0);
                    taskTracker.decrease();
                }
            }
        }
    }

    /**
     * A runnable task to connect the server.
     */
    private class ConnectTask implements Runnable{

        final XmppManager xmppManager;

        private ConnectTask(){
            this.xmppManager = XmppManager.this;
        }

        public void run(){
            LogUtil.i("ConnectTask.run()...");

            if(!xmppManager.isConnected()){
                // Create the configuration for this new connection
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        xmppHost, xmppPort);
                //change by xuyusong  we don't need SASL MARK***************
                connConfig.setSecurityMode(SecurityMode.disabled);
                //connConfig.setSecurityMode(SecurityMode.required);
                connConfig.setSASLAuthenticationEnabled(false);
                connConfig.setCompressionEnabled(false);

                XMPPConnection connection = new XMPPConnection(connConfig);
                xmppManager.setConnection(connection);

                try{
                    // Connect to the server
                    connection.connect();
                    LogUtil.i("XMPP connected successfully");

                    // packet provider
                    ProviderManager.getInstance().addIQProvider("notification",
                            "androidpn:iq:notification",
                            new NotificationIQProvider());
                    xmppManager.runTask(); // 执行下一个任务
                } catch(XMPPException e){
                    LogUtil.e("XMPP connection failed\n" + e);
                    xmppManager.dropTask(2);
                    xmppManager.runTask();
                    xmppManager.startReconnectionThread();
                }
            } else {
                LogUtil.i("XMPP connected already");
                xmppManager.runTask();
            }
        }
    }

    /**
     * A runnable task to register a new user onto the server.
     */
    private class RegisterTask implements Runnable{

        final XmppManager xmppManager;
        boolean isRegisterSucceed;
        boolean hasDropTask;

        private RegisterTask(){
            xmppManager = XmppManager.this;
        }

        @SuppressWarnings("all")
        public void run(){
            LogUtil.i("RegisterTask.run()...");

            if(!xmppManager.isRegistered()){
                isRegisterSucceed = false;
                hasDropTask = false;

                final String newUsername = new DeviceUuidFactory(context).getDeviceUuid().toString(); // newRandomUUID(); // 随机帐号密码
                final String newPassword = "imbest"; // newRandomUUID();

                Registration registration = new Registration(); // Registration -> IQ -> Packet

                PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
                        registration.getPacketID()), new PacketTypeFilter(
                        IQ.class));

                PacketListener packetListener = new PacketListener(){

                    public void processPacket(Packet packet){
                        synchronized(xmppManager){ // 处理服务器返回消息具体逻辑
                            LogUtil.d("processPacket().....");
                            LogUtil.d("packet=" + packet.toXML());

                            if(packet instanceof IQ){
                                IQ response = (IQ)packet;
                                if(response.getType() == IQ.Type.ERROR){
                                    if(!response.getError().toString().contains("409")){
                                        LogUtil.e("Unknown error while registering XMPP account! " + response.getError().getCondition());
                                    } else {
                                        LogUtil.e("Unknown error while registering XMPP account! " + response.getError().getCondition());
                                    }
                                } else if(response.getType() == IQ.Type.RESULT){
                                    xmppManager.setUsername(newUsername);
                                    xmppManager.setPassword(newPassword);
                                    LogUtil.d("username=" + newUsername);
                                    LogUtil.d("password=" + newPassword);

                                    Editor editor = sharedPrefs.edit();
                                    editor.putString(Constants.XMPP_USERNAME, newUsername);
                                    editor.putString(Constants.XMPP_PASSWORD, newPassword);
                                    editor.commit();
                                    isRegisterSucceed = true;
                                    LogUtil.i("Account registered successfully");
                                    //如果已经有任务被删除后，不能让其继续执行
                                    if(!hasDropTask){
                                        xmppManager.runTask();
                                    }
                                }
                            }
                        }
                    }
                };

                connection.addPacketListener(packetListener, packetFilter); // 等待监听

                registration.setType(IQ.Type.SET);
                // registration.setTo(xmppHost);
                // Map<String, String> attributes = new HashMap<String, String>();
                // attributes.put("username", rUsername);
                // attributes.put("password", rPassword);
                // registration.setAttributes(attributes);
                registration.addAttribute("username", newUsername);
                registration.addAttribute("password", newPassword);
                registration.addAttribute("name", getClientDeviceID());
                LogUtil.i("发送注册　username " + newUsername + ", password " + newPassword);
                connection.sendPacket(registration); // 发送注册
                try{
                    Thread.sleep(10 * 1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                synchronized(xmppManager){
                    if(!isRegisterSucceed){
                    // 10s 没有返回注册成功消息，就认为注册失败
                        xmppManager.dropTask(1);
                        hasDropTask = true;
                        xmppManager.runTask();
                        xmppManager.startReconnectionThread();
                    }
                }
            } else {
                LogUtil.i("Account registered already");
                xmppManager.runTask();
            }
        }
    }

    /**
     * A runnable task to log into the server.
     */
    private class LoginTask implements Runnable{

        final XmppManager xmppManager;

        private LoginTask(){
            this.xmppManager = XmppManager.this;
        }

        public void run(){
            LogUtil.i("LoginTask.run()...");

            if(!xmppManager.isAuthenticated()){
                LogUtil.d("username=" + username);
                LogUtil.d("password=" + password);

                try{
                    xmppManager.getConnection().login(
                            xmppManager.getUsername(),
                            xmppManager.getPassword(), XMPP_RESOURCE_NAME);
                    LogUtil.d("Logged in successfully");

                    // connection listener
                    if(xmppManager.getConnectionListener() != null){
                        xmppManager.getConnection().addConnectionListener(
                                xmppManager.getConnectionListener());
                    }

                    // packet filter
                    PacketFilter packetFilter = new PacketTypeFilter(
                            NotificationIQ.class);
                    // packet listener
                    PacketListener packetListener = xmppManager
                            .getNotificationPacketListener();
                    connection.addPacketListener(packetListener, packetFilter); // 过滤推送消息
                    //心跳包的发送
                    LogUtil.i("启动心跳线程");
                    connection.startHeartBeat();
                    //释放xmppManager
                    synchronized(xmppManager){
                        xmppManager.notifyAll();
                    }
                } catch(XMPPException e){
                    LogUtil.e("LoginTask.run()... xmpp error");
                    LogUtil.e("Failed to login to xmpp server. Caused by: "
                                  + e.getMessage());
                    String INVALID_CREDENTIALS_ERROR_CODE = "401";
                    String errorMessage = e.getMessage();
                    if(errorMessage != null
                       && errorMessage
                               .contains(INVALID_CREDENTIALS_ERROR_CODE)){
                        xmppManager.reregisterAccount();
                        return;
                    }
                    xmppManager.startReconnectionThread();

                } catch(Exception e){
                    LogUtil.e("LoginTask.run()... other error");
                    LogUtil.e("Failed to login to xmpp server. Caused by: "
                                  + e.getMessage());
                    xmppManager.startReconnectionThread();
                } finally {
                    xmppManager.runTask();
                }
            } else {
                LogUtil.i("Logged in already");
                xmppManager.runTask();
            }

        }
    }

//    public void reg(Class<? extends HFIntentService> clazz) {
//        LogUtil.i("reg IntentService success");
//        mIntentService = clazz;
//    }

    public Class<? extends HFIntentService> getIntentService(){
        return context.getIntentService();
    }

    public String getClientDeviceID() {
        return context.getClientDeviceID();
    }
}