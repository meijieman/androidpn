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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.hongfans.push.receiver.ConnectivityReceiver;
import com.hongfans.push.util.CommonUtil;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Service that continues to run in background and respond to the push
 * notification events from the server. This should be registered as mIntentService
 * in AndroidManifest.xml.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationService extends Service{

    public static final String ACTION_SET_CLIENT_DEVICE_ID = "action_set_client_device_id";
    public static final String ACTION_SET_UIS = "action_set_uis";

    public static final String SERVICE_NAME = "org.androidpn.client.NotificationService";

    private static NotificationService notificationService;

    private Class<? extends HFIntentService> mIntentService;

    private TelephonyManager telephonyManager;

    //    private WifiManager wifiManager;
    //
    //    private ConnectivityManager connectivityManager;

//    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver connectivityReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private XmppManager xmppManager;

    private SharedPreferences sharedPrefs;

//    private String deviceId;
    private String mClientDeviceID;

    public NotificationService(){
//        notificationReceiver = new NotificationReceiver();
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }

    //用于返回这个服务的实例
    public static NotificationService getNotification(){
        return notificationService;
    }

    @Override
    public void onCreate(){
        LogUtil.d("onCreate()...");
        notificationService = this;
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get deviceId
//        deviceId = telephonyManager.getDeviceId();
//        LogUtil.d("deviceId=" + deviceId);
//        Editor editor = sharedPrefs.edit();
//        editor.putString(Constants.DEVICE_ID, deviceId);
//        editor.commit();
//
//        // If running on an emulator
//        if(deviceId == null || deviceId.trim().length() == 0
//           || deviceId.matches("0+")){
//            if(sharedPrefs.contains("EMULATOR_DEVICE_ID")){
//                deviceId = sharedPrefs.getString(Constants.EMULATOR_DEVICE_ID, "");
//            } else {
//                deviceId = (new StringBuilder("EMU"))
//                        .append((new Random(System.currentTimeMillis())).nextLong())
//                        .toString();
//                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
//                editor.commit();
//            }
//        }

        startForeground(0, null);
        xmppManager = new XmppManager(this);

//        registerNotificationReceiver();
        registerConnectivityReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("onStartCommand()... " + intent.getAction());
        if (XMPPConnection.PENDING_START_ALARM_ACTION.equals(intent.getAction())) {
            xmppManager.broadcastHeartBeat();
        } else if (NotificationService.ACTION_SET_UIS.equalsIgnoreCase(intent.getAction())) {
            String uis = intent.getStringExtra("uis");
            if (CommonUtil.isNotEmpty(uis)) {
                try {
                    mIntentService = (Class<HFIntentService>)Class.forName(uis);
                    LogUtil.i("获取到应用端定义的 IntentService " + mIntentService);
                } catch(ClassNotFoundException e) {
//                e.printStackTrace();
                    LogUtil.e("do not set IntentService");
                }
            }
        } else if (NotificationService.ACTION_SET_CLIENT_DEVICE_ID.equalsIgnoreCase(intent.getAction())) {
            String clientDeviceID = intent.getStringExtra("clientDeviceID");
            if (CommonUtil.isNotEmpty(clientDeviceID)) {
                mClientDeviceID = clientDeviceID;
                LogUtil.i("获取到应用端定义的唯一标识 " + mClientDeviceID);
            }
            //　启动登录流程
            taskSubmitter.submit(new Runnable() {
                public void run() {
                    start();
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        LogUtil.d("onDestroy()...");
        stopForeground(false);
        notificationService = null;
        stop();
//        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        LogUtil.d("onBind()...");
        return null;
    }

    @Override
    public void onRebind(Intent intent){
        LogUtil.d("onRebind()...");
    }

    @Override
    public boolean onUnbind(Intent intent){
        LogUtil.d("onUnbind()...");
        return true;
    }

    public Class<? extends HFIntentService> getIntentService(){
        return mIntentService;
    }

    public String getClientDeviceID(){
        LogUtil.i("获取　getClientDeviceID " + mClientDeviceID);
        return mClientDeviceID;
    }

    public static Intent getIntent(Context ctx, String action){
        Intent intent = new Intent(ctx, NotificationService.class);
        if (action != null) {
            intent.setAction(action);
        }
        return intent; // new Intent(SERVICE_NAME);
    }

    public ExecutorService getExecutorService(){
        return executorService;
    }

    public TaskSubmitter getTaskSubmitter(){
        return taskSubmitter;
    }

    public TaskTracker getTaskTracker(){
        return taskTracker;
    }

    public XmppManager getXmppManager(){
        return xmppManager;
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPrefs;
    }

//    public String getDeviceId(){
//        return deviceId;
//    }

    public void connect(){
        LogUtil.d("connect()...");
        taskSubmitter.submit(new Runnable(){
            public void run(){
                NotificationService.this.getXmppManager().connect();
            }
        });
    }

    public void disconnect(){
        LogUtil.d("disconnect()...");
        taskSubmitter.submit(new Runnable(){
            public void run(){
                NotificationService.this.getXmppManager().disconnect();
            }
        });
    }

//    private void registerNotificationReceiver(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
//        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
//        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
//        registerReceiver(notificationReceiver, filter);
//    }

//    private void unregisterNotificationReceiver(){
//        unregisterReceiver(notificationReceiver);
//    }

    private void registerConnectivityReceiver(){
        LogUtil.d("registerConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        // filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver(){
        LogUtil.d("unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }

    private void start(){
        LogUtil.d("start()...");
        // Intent intent = getIntent();
        // startService(intent);
        xmppManager.connect();
    }

    private void stop(){
        LogUtil.d("stop()...");
        xmppManager.disconnect();
        executorService.shutdown();
    }

    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter{

        final NotificationService notificationService;

        public TaskSubmitter(NotificationService notificationService){
            this.notificationService = notificationService;
        }

        @SuppressWarnings("unchecked")
        public Future submit(Runnable task){
            Future result = null;
            if(!notificationService.getExecutorService().isTerminated()
               && !notificationService.getExecutorService().isShutdown()
               && task != null){
                result = notificationService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker{

        final NotificationService notificationService;

        public int count;

        public TaskTracker(NotificationService notificationService){
            this.notificationService = notificationService;
            this.count = 0;
        }

        public void increase(){
            synchronized(notificationService.getTaskTracker()){
                notificationService.getTaskTracker().count++;
                LogUtil.d("Incremented task count to " + count);
            }
        }

        public void decrease(){
            synchronized(notificationService.getTaskTracker()){
                notificationService.getTaskTracker().count--;
                LogUtil.d("Decremented task count to " + count);
            }
        }
    }
}
