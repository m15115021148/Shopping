package com.geek.shopping.ui;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.adapter.MyIssueAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;

import java.util.List;

import butterknife.BindView;

public class MyIssueActivity extends BaseActivity {
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;

    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private MyIssueAdapter mAdapter;

    @BindView(R.id.rightLayout)
    public LinearLayout mRightLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_issue;
    }

    @Override
    protected void initData() {
        mBack.setOnClickListener(this);
        mTitle.setText("我的发布");
        mRightLayout.setOnClickListener(this);
        initRegisterView();

        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) finish();
        if (v == mRightLayout){
            Intent intent = new Intent(this,IssueActivity.class);
            startActivity(intent);
        }
    }

    private void initRegisterView(){
        TextView tv = new TextView(this);
        tv.setText("发布");
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.white));
        mRightLayout.addView(tv);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyIssueAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ProductModel> list = MyApplication.getInstance().mDbIssue.getUserData(ConfigUtil.USER_ID);
        if (list != null && list.size()>0){
            mAdapter.setData(list);
        }
    }
}
