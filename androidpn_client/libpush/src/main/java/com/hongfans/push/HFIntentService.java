package com.hongfans.push;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hongfans.push.logutil.LogUtil;

/**
 * 封装消息
 * Created by MEI on 2017/11/4.
 */
public abstract class HFIntentService extends IntentService {

    public static final String ACTION_RCVD = "ACTION_RCVD";
    public static final String TRANSMIT_DATA = "transmit_data";

    public HFIntentService() {
        super("HFIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.i("onHandleIntent intent " + intent);
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_RCVD:
                        onReceiveMessageData(this, (Notification) intent.getSerializableExtra(TRANSMIT_DATA));
                        break;
                }
            }
        }
    }

//    public abstract void onReceiveServicePid(Context var1, int var2);

//    public abstract void onReceiveClientId(Context var1, String var2);

    public abstract void onReceiveMessageData(Context ctx, Notification notification);

//    public abstract void onReceiveOnlineState(Context var1, boolean var2);

//    public abstract void onReceiveCommandResult(Context var1, GTCmdMessage var2);
}
