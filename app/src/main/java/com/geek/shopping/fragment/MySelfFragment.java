package com.geek.shopping.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.view.MyImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * 我的
 */
public class MySelfFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.header)
    public MyImageView mHeader;
    @BindView(R.id.nickName)
    public TextView mNickName;
    @BindView(R.id.address)
    public TextView mAddress;
    @BindView(R.id.issue)
    public TextView mIssue;
    @BindView(R.id.order)
    public TextView mOrder;
    @BindViews({R.id.issueLayout,R.id.orderLayout,R.id.addressLayout,R.id.aboutLayout})
    public List<RelativeLayout> mReList;

    @Override
    protected int setContentView() {
        return R.layout.fragment_myself;
    }

    @Override
    protected void initData() {
        mHeader.setOnClickListener(this);
        mReList.get(0).setOnClickListener(this);
        mReList.get(1).setOnClickListener(this);
        mReList.get(2).setOnClickListener(this);
        mReList.get(3).setOnClickListener(this);
    }

    @Override
    protected void startLoad() {

    }

    @Override
    public void onClick(View v) {
        if (v == mHeader){

        }
        if (v == mReList.get(0)){

        }
        if (v == mReList.get(1)){

        }
        if (v == mReList.get(2)){

        }
        if (v == mReList.get(3)){

        }
    }
}
