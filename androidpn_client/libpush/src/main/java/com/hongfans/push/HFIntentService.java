package com.hongfans.push;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hongfans.push.message.Notification;
import com.hongfans.push.message.Payload;
import org.jivesoftware.smack.util.LogUtil;

import java.io.Serializable;

/**
 * 封装消息
 * Created by MEI on 2017/11/4.
 */
public abstract class HFIntentService extends IntentService {

    public static final String ACTION_RCVD = "action_rcvd";
    public static final String TRANSMIT_DATA = "transmit_data";

    public HFIntentService() {
        super("HFIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.i("onHandleIntent intent " + intent);
        if(intent != null){
            final String action = intent.getAction();
            if(action != null){
                if(ACTION_RCVD.equalsIgnoreCase(action)){
                    Serializable extra = intent.getSerializableExtra(TRANSMIT_DATA);
                    LogUtil.i("onHandleIntent extra " + extra);
                    if(extra instanceof Notification){
                        onReceiveNotification(this, (Notification)extra);
                    } else if(extra instanceof Payload){
                        onReceivePayload(this, (Payload)extra);
                    }
                }
            }
        }
    }

//    public abstract void onReceiveServicePid(Context var1, int var2);

//    public abstract void onReceiveClientId(Context var1, String var2);

    /**
     * 通知
     */
    public abstract void onReceiveNotification(Context ctx, Notification note);

    /**
     * 穿透消息
     */
    public abstract void onReceivePayload(Context ctx, Payload payload);

//    public abstract void onReceiveOnlineState(Context var1, boolean var2);

//    public abstract void onReceiveCommandResult(Context var1, GTCmdMessage var2);
}
