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
package org.androidpn.demoapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hongfans.push.ServiceManager;

import java.util.Date;

/**
 * This is an androidpn client demo application.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class DemoAppActivity extends Activity{

    private TextView tv;
    private TextView info;

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            tv.setText(new Date().toLocaleString() + "\n" + intent.getStringExtra(DemoIntentService.EXTRA_PAYLOAD));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d("DemoAppActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView)findViewById(R.id.tv_main);
        info = (TextView) findViewById(R.id.tv_info);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DemoIntentService.ACTION_PAYLOAD);
        registerReceiver(mReceiver, filter);

        // Start the service
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.startService();
        serviceManager.registerPushIntentService(DemoIntentService.class);
        serviceManager.setAlias("xuyusong-mi"); // 需要保证唯一，否则设置不成功
        serviceManager.setTags(new String[]{"game", "music", "computer"});
//        serviceManager.registerPushIntentService(DemoIntentService.class); // 需要 startService 后 invoke

        info.setText(serviceManager.getVersion());
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}