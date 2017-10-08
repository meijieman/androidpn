package org.jivesoftware.smack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.dns.DNSJavaResolver;
import org.jivesoftware.smackx.ConfigureProviderManager;
import org.jivesoftware.smackx.InitStaticCode;
import org.xbill.DNS.ResolverConfig;

public class SmackAndroid{

    private static SmackAndroid sSmackAndroid = null;

    private BroadcastReceiver mConnectivityChangedReceiver;
    private Context mCtx;

    private SmackAndroid(Context ctx){
        mCtx = ctx;
        DNSUtil.setDNSResolver(DNSJavaResolver.getInstance());
        InitStaticCode.initStaticCode(ctx);
        ConfigureProviderManager.configureProviderManager();
        maybeRegisterReceiver();
    }

    private void maybeRegisterReceiver(){
        if(mConnectivityChangedReceiver == null){
            mConnectivityChangedReceiver = new ConnectivtyChangedReceiver();
            mCtx.registerReceiver(mConnectivityChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    public static SmackAndroid init(Context ctx){
        if(sSmackAndroid == null){
            sSmackAndroid = new SmackAndroid(ctx);
        } else {
            sSmackAndroid.maybeRegisterReceiver();
        }
        return sSmackAndroid;
    }

    public void onDestroy(){
        if(mConnectivityChangedReceiver != null){
            mCtx.unregisterReceiver(mConnectivityChangedReceiver);
            mConnectivityChangedReceiver = null;
        }
    }

    class ConnectivtyChangedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            ResolverConfig.refresh();
        }

    }
}
