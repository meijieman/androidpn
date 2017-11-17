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
package com.hongfans.push.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.hongfans.push.Constants;
import com.hongfans.push.message.Payload;

import java.io.Serializable;
import java.util.Random;

import static android.os.Looper.getMainLooper;

/**
 * This class is to notify the user of messages with NotificationManager.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class Notifier{

    private static final Random random = new Random(System.currentTimeMillis());

    private Context context;
    private Handler mHandler = new Handler(getMainLooper());

    private SharedPreferences sharedPrefs;

    private NotificationManager notificationManager;

    public Notifier(Context context){
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notify(final com.hongfans.push.message.Notification note){
        LogUtil.d("notify()...");

        LogUtil.d("notification=" + note);

        if(isNotificationEnabled()){
            // Show the toast
            if(isNotificationToastEnabled()){
                mHandler.post(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(context, note.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            // Notification
            Notification notification = new Notification();
            notification.icon = getNotificationIcon();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            if(isNotificationSoundEnabled()){
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if(isNotificationVibrateEnabled()){
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.when = System.currentTimeMillis();
            notification.tickerText = note.getMessage();

            //            Intent intent;
            //            if (uri != null
            //                    && uri.length() > 0
            //                    && (uri.startsWith("http:") || uri.startsWith("https:")
            //                            || uri.startsWith("tel:") || uri.startsWith("geo:"))) {
            //                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //            } else {
            //                String callbackActivityPackageName = sharedPrefs.getString(
            //                        Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
            //                String callbackActivityClassName = sharedPrefs.getString(
            //                        Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");
            //                intent = new Intent().setClassName(callbackActivityPackageName,
            //                        callbackActivityClassName);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //            }

            Intent intent = new Intent();
            if(mOpen != null){
                intent.setClass(context, mOpen);
                intent.putExtra(NOTIFICATION, note);
//                intent.putExtra(Constants.NOTIFICATION_ID, note.getId());
//                intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
//                intent.putExtra(Constants.NOTIFICATION_TITLE, note.getTitle());
//                intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
//                intent.putExtra(Constants.NOTIFICATION_URI, note.getUri());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

//          PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // bugfix 当服务端连续发送多条通知时，客户端都是显示同一条通知内容。
            PendingIntent contentIntent = PendingIntent.getActivity(context, new Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setLatestEventInfo(context, note.getTitle(), note.getMessage(), contentIntent);

            notificationManager.notify(random.nextInt(), notification);

            //            Intent clickIntent = new Intent(
            //                    Constants.ACTION_NOTIFICATION_CLICKED);
            //            clickIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            //            clickIntent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
            //            clickIntent.putExtra(Constants.NOTIFICATION_TITLE, title);
            //            clickIntent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
            //            clickIntent.putExtra(Constants.NOTIFICATION_URI, uri);
            //            //        positiveIntent.setData(Uri.parse((new StringBuilder(
            //            //                "notif://notification.adroidpn.org/")).append(apiKey).append(
            //            //                "/").append(System.currentTimeMillis()).toString()));
            //            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(
            //                    context, 0, clickIntent, 0);
            //
            //            notification.setLatestEventInfo(context, title, message,
            //                    clickPendingIntent);
            //
            //            Intent clearIntent = new Intent(
            //                    Constants.ACTION_NOTIFICATION_CLEARED);
            //            clearIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            //            clearIntent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
            //            //        negativeIntent.setData(Uri.parse((new StringBuilder(
            //            //                "notif://notification.adroidpn.org/")).append(apiKey).append(
            //            //                "/").append(System.currentTimeMillis()).toString()));
            //            PendingIntent clearPendingIntent = PendingIntent.getBroadcast(
            //                    context, 0, clearIntent, 0);
            //            notification.deleteIntent = clearPendingIntent;
            //
            //            notificationManager.notify(random.nextInt(), notification);

        } else {
            LogUtil.w("Notificaitons disabled.");
        }
    }

    private int getNotificationIcon(){
        int id = context.getResources().getIdentifier("notification", "drawable", context.getPackageName());
        return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, id);
    }

    private boolean isNotificationEnabled(){
        return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED, true);
    }

    private boolean isNotificationSoundEnabled(){
        return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
    }

    private boolean isNotificationVibrateEnabled(){
        return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
    }

    private boolean isNotificationToastEnabled(){
        return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
    }

    public void setNotificationEnabled(boolean isEnable){
        sharedPrefs.edit().putBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED, isEnable).commit();
    }

    public void setNotificationSoundEnabled(boolean isEnable){
        sharedPrefs.edit().putBoolean(Constants.SETTINGS_SOUND_ENABLED, isEnable).commit();
    }

    public void setNotificationVibrateEnabled(boolean isEnable){
        sharedPrefs.edit().putBoolean(Constants.SETTINGS_VIBRATE_ENABLED, isEnable).commit();
    }

    public void setNotificationToastEnabled(boolean isEnable){
        sharedPrefs.edit().putBoolean(Constants.SETTINGS_TOAST_ENABLED, isEnable).commit();
    }

    public void setNotificationIcon(int iconId){
        sharedPrefs.edit().putInt(Constants.NOTIFICATION_ICON, iconId).commit();
    }

    public void setAutoStart(boolean autoStart){
        sharedPrefs.edit().putBoolean(Constants.SETTINGS_AUTO_START, autoStart).commit();
    }

    private Class mOpen; // 点击时打开的 activity

    public void setOpen(Class<? extends Activity> open){
        mOpen = open;
    }

    private static final String NOTIFICATION = "notification";

    public static com.hongfans.push.message.Notification getNotification(Intent intent){
        Serializable extra = intent.getSerializableExtra(NOTIFICATION);
        if(extra instanceof com.hongfans.push.message.Notification){
            return (com.hongfans.push.message.Notification)extra;
        }
        return null;
    }

    public void showAlertDialog(final Context context, final Payload payload) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(payload.getUuid())
                        .setMessage(payload.getMessage())
                        .setPositiveButton("确定", null)
                        .create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }
}
