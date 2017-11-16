package com.major.pushtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hongfans.push.ServiceManager;
import com.hongfans.push.util.CommonUtil;
import com.hongfans.push.util.LogUtil;

public class MainActivity extends Activity implements View.OnClickListener{

    private ServiceManager serviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView)findViewById(R.id.tv_version);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_set_alias).setOnClickListener(this);
        findViewById(R.id.btn_set_tags).setOnClickListener(this);
        findViewById(R.id.btn_clear_sp).setOnClickListener(this);

        serviceManager = new ServiceManager(this);
        String msg = "sdk 版本: " + serviceManager.getVersion();
        text.setText(msg);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btn_start:
                LogUtil.i("启动 push");
                serviceManager.startService("HFWL0000000000");
                serviceManager.registerPushIntentService(DemoIntentService.class);
                break;
            case R.id.btn_stop:
                LogUtil.i("停止 push");
                serviceManager.stopService();
                break;
            case R.id.btn_set_alias: {
                final EditText et = new EditText(this);
                et.setHintTextColor(Color.LTGRAY);
                et.setHint("输入别名");
                new AlertDialog.Builder(this)
                        .setTitle("设置")
                        .setView(et)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                String trim = et.getText().toString().trim();
                                serviceManager.setAlias(trim); // trim 为空则为删除别名
                                if(CommonUtil.isNotEmpty(trim)){
                                    Toast.makeText(MainActivity.this, "设置别名成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "删除别名成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            }
            case R.id.btn_set_tags: {
                final EditText et = new EditText(this);
                et.setHintTextColor(Color.LTGRAY);
                et.setHint("输入标签，多个标签以 , 分隔");
                new AlertDialog.Builder(this)
                        .setTitle("设置")
                        .setView(et)
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                String trim = et.getText().toString().trim();
                                if(CommonUtil.isNotEmpty(trim)){
                                    String[] split = trim.split(",");
                                    serviceManager.setTags(split);
                                    Toast.makeText(MainActivity.this, "设置标签成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            }
            case R.id.btn_clear_sp:
//                serviceManager.clearUserInfo();
                break;
        }
    }
}
