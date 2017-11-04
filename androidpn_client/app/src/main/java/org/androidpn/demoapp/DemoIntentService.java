package org.androidpn.demoapp;

import android.content.Context;

import com.hongfans.push.HFIntentService;
import com.hongfans.push.Notification;
import com.hongfans.push.display.Notifier;
import com.hongfans.push.logutil.LogUtil;

/**
 * TODO
 * Created by MEI on 2017/11/4.
 */

public class DemoIntentService extends HFIntentService {

    @Override
    public void onReceiveMessageData(Context ctx, Notification notification) {
        LogUtil.i("notification " + notification);

        // 通知栏
        Notifier notifier = new Notifier(ctx);
        notifier.setNotificationEnabled(true);
        notifier.setNotificationSoundEnabled(true);
        notifier.setNotificationVibrateEnabled(true);
        notifier.setNotificationToastEnabled(true);
        notifier.notify(notification.getId(), notification.getApiKey(), notification.getTitle(), notification.getMessage(), notification.getUri());
    }
}
