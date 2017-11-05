package org.androidpn.demoapp;

import android.content.Context;
import android.content.Intent;

import com.hongfans.push.HFIntentService;
import com.hongfans.push.message.Notification;
import com.hongfans.push.message.Payload;
import com.hongfans.push.util.Notifier;

/**
 * TODO
 * Created by MEI on 2017/11/4.
 */

public class DemoIntentService extends HFIntentService {

    public static final String ACTION_PAYLOAD = "org.androidpn.demoapp.payload";
    public static final String EXTRA_PAYLOAD = "payload";

    @Override
    public void onReceiveNotification(Context ctx, Notification note){
        // 通知栏
        Notifier notifier = new Notifier(ctx);
//        notifier.setNotificationIcon(R.drawable.icon);
        notifier.setNotificationEnabled(true);
        notifier.setNotificationSoundEnabled(true);
        notifier.setNotificationVibrateEnabled(true);
        notifier.setNotificationToastEnabled(false);
        notifier.setOpen(NotificationDetailsActivity.class);
        notifier.notify(note);
    }

    @Override
    public void onReceivePayload(Context ctx, Payload payload){
        // 应用内展示
        Intent intent = new Intent();
        intent.setAction(ACTION_PAYLOAD);
        intent.putExtra(EXTRA_PAYLOAD, payload.toString());
        sendBroadcast(intent);
    }
}
