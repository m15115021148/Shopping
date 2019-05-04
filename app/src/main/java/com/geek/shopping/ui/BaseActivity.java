package com.geek.shopping.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类activity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG_ESC_ACTIVITY = "com.broader.esc";//内容描述 退出activity时 发送的广播信号
    private MyBroaderEsc receiver;//广播
    private Unbinder butterKnife;//取消绑定

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {//设置为竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // 注册广播
        receiver = new MyBroaderEsc();
        registerReceiver(receiver, new IntentFilter(TAG_ESC_ACTIVITY));
        // 反射注解机制初始化
        butterKnife = ButterKnife.bind(this);
        initData();
    }

    protected abstract int getLayoutId();//获取布局layout

    protected abstract void initData();//初始化数据

    /**
     * 退出app
     */
    protected void exitApp(){
        Intent intent = new Intent();
        intent.setAction(TAG_ESC_ACTIVITY);
        sendBroadcast(intent);
    }

    /**
     * @发送广播 退出activity
     *
     */
    class MyBroaderEsc extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                butterKnife.unbind();
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {

    }
}
