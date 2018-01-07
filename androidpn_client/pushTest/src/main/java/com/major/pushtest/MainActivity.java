package com.major.pushtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hongfans.push.ServiceManager;
import com.hongfans.push.util.CommonUtil;

import org.jivesoftware.smack.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private ServiceManager serviceManager;
    private ArrayAdapter<Spanned> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView)findViewById(R.id.lv_log);
        final List<Spanned> data = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, android.R.id.text1, data);
        listView.setAdapter(mAdapter);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_set_alias).setOnClickListener(this);
        findViewById(R.id.btn_set_tags).setOnClickListener(this);
        findViewById(R.id.btn_clear_sp).setOnClickListener(this);
        findViewById(R.id.btn_clear_log).setOnClickListener(this);
        final CheckBox cb = (CheckBox)findViewById(R.id.cb_auto_scroll);

        LogUtil.setLogListener(new LogUtil.LogListener() {
            @Override
            public void log(final String level, final Object msg, final String timestamp) {
                String color = "#DDDDDD";
                if ("d".equalsIgnoreCase(level)) {
                    color = "#BBBBBB";
                } else if ("i".equalsIgnoreCase(level)) {
                    color = "#36BB1D";
                } else if ("w".equalsIgnoreCase(level)) {
                    color = "#BB9C22";
                } else if ("e".equalsIgnoreCase(level)) {
                    color = "#FF6B68";
                }

                final Spanned spanned = Html.fromHtml("<font color=\"" + color + "\">" + level + ": " + timestamp + " " + msg + "</font>");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data.add(spanned);
                        mAdapter.notifyDataSetChanged();
                        if (cb.isChecked()) {
                            listView.smoothScrollToPosition(mAdapter.getCount());
                        }
                    }
                });
            }
        });

        serviceManager = new ServiceManager(this);
    }

    @Override
    public void onClick(View v) {
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
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = et.getText().toString().trim();
                                serviceManager.setAlias(trim); // trim 为空则为删除别名
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
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = et.getText().toString().trim();
                                if (CommonUtil.isNotEmpty(trim)) {
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
            case R.id.btn_clear_log:
                mAdapter.clear();
                break;
        }
    }
}
