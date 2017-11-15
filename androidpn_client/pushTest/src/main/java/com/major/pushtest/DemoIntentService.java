package com.major.pushtest;

import android.content.Context;

import com.hongfans.push.HFIntentService;
import com.hongfans.push.message.Notification;
import com.hongfans.push.message.Payload;
import com.hongfans.push.util.Notifier;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/11/14 22:26
 */
public class DemoIntentService extends HFIntentService{

    @Override
    public void onReceiveNotification(Context context, Notification notification){
        Notifier notifier = new Notifier(context);
        notifier.setNotificationEnabled(true);
        notifier.setNotificationSoundEnabled(true);
        notifier.setNotificationVibrateEnabled(true);
        notifier.setNotificationToastEnabled(false);
        notifier.setOpen(MainActivity.class);
        notifier.notify(notification);
    }

    @Override
    public void onReceivePayload(Context context, Payload payload){
        Notifier notifier = new Notifier(context);
        notifier.showAlertDialog(context, payload);
    }
}
