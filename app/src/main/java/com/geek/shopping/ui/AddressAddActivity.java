package com.geek.shopping.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;

import butterknife.BindView;

public class AddressAddActivity extends BaseActivity {
    @BindView(R.id.back)
    public LinearLayout mBack;
    @BindView(R.id.title)
    public TextView mTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void initData() {
        mTitle.setText("添加地址");
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            finish();
        }

    }

}
