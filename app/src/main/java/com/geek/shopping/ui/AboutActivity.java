package com.geek.shopping.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.util.PreferencesUtil;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        mBack.setOnClickListener(this);
        mTitle.setText("关于我们");
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            finish();
        }

    }

    public void onExit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出账号吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                MainActivity.mContext.finish();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
}
