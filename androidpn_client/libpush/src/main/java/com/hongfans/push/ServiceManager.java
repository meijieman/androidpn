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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hongfans.push.iq.SetAliasIQ;
import com.hongfans.push.iq.SetTagsIQ;
import com.hongfans.push.util.CommonUtil;
import com.hongfans.push.util.LogUtil;

import org.jivesoftware.smack.packet.IQ;

import java.util.Properties;

/**
 * This class is to manage the notificatin service and to load the configuration.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class ServiceManager{

    private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

    private Context context;

    private SharedPreferences sharedPrefs;

    private Properties props;

    private String version;

    private String apiKey;

    private String xmppHost;

    private String xmppPort;

//    private String callbackActivityPackageName;

//    private String callbackActivityClassName;

    public ServiceManager(Context context){
        this.context = context;

//        if(context instanceof Activity){
//            LogUtil.i("Callback Activity...");
//            Activity callbackActivity = (Activity)context;
//            callbackActivityPackageName = callbackActivity.getPackageName();
//            callbackActivityClassName = callbackActivity.getClass().getName();
//        }

        //        apiKey = getMetaDataValue("ANDROIDPN_API_KEY");
        //        LogUtil.i("apiKey=" + apiKey);
        //        //        if (apiKey == null) {
        //        //            LogUtil.e("Please set the androidpn api key in the manifest file.");
        //        //            throw new RuntimeException();
        //        //        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "");
        xmppHost = props.getProperty("xmppHost", "127.0.0.1");
        xmppPort = props.getProperty("xmppPort", "5222");
        version = props.getProperty("version", "0.5.0") + BuildConfig.BUILD_TIMESTAMP;
        LogUtil.i("apiKey=" + apiKey);
        LogUtil.i("xmppHost=" + xmppHost);
        LogUtil.i("xmppPort=" + xmppPort);
        LogUtil.i("version=" + version);

        sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
//        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
//                callbackActivityPackageName);
//        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
//                callbackActivityClassName);
        editor.commit();
        // LogUtil.i("sharedPrefs=" + sharedPrefs.toString());
    }

    public void startService(){
//        Thread serviceThread = new Thread(new Runnable(){
//            @Override
//            public void run(){
                Intent intent = NotificationService.getIntent(context);
                context.startService(intent);
//            }
//        });
//        serviceThread.start();
    }

    public void stopService(){
        Intent intent = NotificationService.getIntent(context);
        context.stopService(intent);
    }

    //    private String getMetaDataValue(String name, String def) {
    //        String value = getMetaDataValue(name);
    //        return (value == null) ? def : value;
    //    }
    //
    //    private String getMetaDataValue(String name) {
    //        Object value = null;
    //        PackageManager packageManager = context.getPackageManager();
    //        ApplicationInfo applicationInfo;
    //        try {
    //            applicationInfo = packageManager.getApplicationInfo(context
    //                    .getPackageName(), 128);
    //            if (applicationInfo != null && applicationInfo.metaData != null) {
    //                value = applicationInfo.metaData.get(name);
    //            }
    //        } catch (NameNotFoundException e) {
    //            throw new RuntimeException(
    //                    "Could not read the name in the manifest file.", e);
    //        }
    //        if (value == null) {
    //            throw new RuntimeException("The name '" + name
    //                    + "' is not defined in the manifest file's meta data.");
    //        }
    //        return value.toString();
    //    }

    private Properties loadProperties(){
        //        InputStream in = null;
        //        Properties props = null;
        //        try {
        //            in = getClass().getResourceAsStream(
        //                    "/org/androidpn/client/client.properties");
        //            if (in != null) {
        //                props = new Properties();
        //                props.load(in);
        //            } else {
        //                LogUtil.e("Could not find the properties file.");
        //            }
        //        } catch (IOException e) {
        //            LogUtil.e("Could not find the properties file.", e);
        //        } finally {
        //            if (in != null)
        //                try {
        //                    in.close();
        //                } catch (Throwable ignore) {
        //                }
        //        }
        //        return props;

        Properties props = new Properties();
        try{
            int id = context.getResources().getIdentifier("androidpn", "raw", context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch(Exception e){
            LogUtil.e("Could not find the properties file." + e);
            // e.printStackTrace();
        }
        return props;
    }

    public String getVersion(){
        return version;
    }
    //
    //    public String getApiKey() {
    //        return apiKey;
    //    }

//    public void setNotificationIcon(int iconId){
//        Editor editor = sharedPrefs.edit();
//        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
//        editor.commit();
//    }

    //    public void viewNotificationSettings() {
    //        Intent intent = new Intent().setClass(context,
    //                NotificationSettingsActivity.class);
    //        context.startActivity(intent);
    //    }

   /* public static void viewNotificationSettings(Context context){
        Intent intent = new Intent().setClass(context, NotificationSettingsActivity.class);
        context.startActivity(intent);
    }*/

    //设置别名
    public void setAlias(final String alias){
        final String username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
        if(CommonUtil.isEmpty(alias) || CommonUtil.isEmpty(username)){
            return;
        }
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(500);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                NotificationService service = NotificationService.getNotification();
                if (service == null) {
                    LogUtil.e("service is null, set alias failed");
                    return;
                }
                XmppManager xmppManager = service.getXmppManager();
                if(xmppManager != null){
                    if(!xmppManager.isAuthenticated()){
                        synchronized(xmppManager){
                            try{
                                LogUtil.d("wait for authenticate");
                                xmppManager.wait();
                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.d("authenticated");
                    SetAliasIQ setAliasIQ = new SetAliasIQ();
                    setAliasIQ.setType(IQ.Type.SET);
//                    setAliasIQ.setUsername(username);
                    setAliasIQ.setAlias(alias);
                    LogUtil.d("username " + username + ", alias " + alias);
                    xmppManager.getConnection().sendPacket(setAliasIQ);
                }
            }
        }).start();
    }

    public <T extends HFIntentService> void registerPushIntentService(final Class<T> service) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NotificationService notificationService = NotificationService.getNotification();
                if (notificationService == null) {
                    LogUtil.e("service is null, register IntentService failed");
                    return;
                }
                XmppManager xmppManager = notificationService.getXmppManager();
                if (xmppManager != null) {
                    if (!xmppManager.isAuthenticated()) {
                        synchronized (xmppManager) {
                            try {
                                LogUtil.d("wait for authenticate");
                                xmppManager.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.d("authenticated");

                    xmppManager.reg(service);
                }
            }
        }).start();
    }

    public void setTags(final String[] tags){
        final String username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
        if(CommonUtil.isEmpty(tags) || CommonUtil.isEmpty(username)){
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                for(String tag : tags){
                    sb.append(tag).append(",");
                }
                String tmp = sb.toString();
                String tag = tmp.substring(0, tmp.length() - 1);
                NotificationService service = NotificationService.getNotification();
                if (service == null) {
                    LogUtil.e("service is null, set tags failed");
                    return;
                }
                XmppManager xmppManager = service.getXmppManager();
                if (xmppManager != null) {
                    if (!xmppManager.isAuthenticated()) {
                        synchronized (xmppManager) {
                            try {
                                LogUtil.d("wait for authenticate");
                                xmppManager.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.d("authenticated");
                    SetTagsIQ iq = new SetTagsIQ();
                    iq.setType(IQ.Type.SET);
                    iq.setTags(tag);
                    LogUtil.d("username " + username + ", tags " + tag);
                    xmppManager.getConnection().sendPacket(iq);
                }
            }
        }).start();
    }
}
