package com.geek.shopping.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;

import butterknife.BindView;

public class AddressActivity extends BaseActivity {
    @BindView(R.id.back)
    public LinearLayout mBack;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.rightLayout)
    public LinearLayout mRightLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initData() {
        mTitle.setText("地址管理");
        mBack.setOnClickListener(this);
        initRightView();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            finish();
        }

    }

    private void initRightView(){
        TextView tv = new TextView(this);
        tv.setText("添加");
        tv.setTextSize(12);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this,AddressAddActivity.class);
                startActivity(intent);
            }
        });
        mRightLayout.addView(tv);
    }

}
