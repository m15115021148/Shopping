package com.geek.shopping.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.database.entity.UserModel;
import com.geek.shopping.ui.AboutActivity;
import com.geek.shopping.ui.AccountActivity;
import com.geek.shopping.ui.MyIssueActivity;
import com.geek.shopping.util.ImageUtil;
import com.geek.shopping.util.PreferencesUtil;
import com.geek.shopping.util.ToastUtil;
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
    public void onResume() {
        super.onResume();
        List<ProductModel> list = MyApplication.getInstance().mDbIssue.getUserData(ConfigUtil.USER_ID);
        if (list != null){
            mIssue.setText(String.format(getString(R.string.my_self_issue_number),list.size()));
        }
        mOrder.setText(String.format(getString(R.string.my_self_order_number),0));

        updateUser();
    }

    @Override
    public void onClick(View v) {
        if (v == mHeader){
            Intent intent = new Intent(getContext(), AccountActivity.class);
            startActivity(intent);
        }
        if (v == mReList.get(0)){
            Intent intent = new Intent(getContext(), MyIssueActivity.class);
            startActivity(intent);
        }
        if (v == mReList.get(1)){
            ToastUtil.showShort(getContext(),"开发中");
        }
        if (v == mReList.get(2)){
            ToastUtil.showShort(getContext(),"开发中");
        }
        if (v == mReList.get(3)){
            Intent intent = new Intent(getContext(), AboutActivity.class);
            startActivity(intent);
        }
    }

    private void updateUser(){
        UserModel model = MyApplication.getInstance().mDbUser.getUserData(PreferencesUtil.getStringData(getContext(), ConfigUtil.KEY_PHONE));
        if (model == null)return;
        ImageUtil.loadCircleImage(
                getContext(),model.getHeaderImg(),R.drawable.head_defaut,R.drawable.head_defaut,mHeader
        );
        mNickName.setText(TextUtils.isEmpty(model.getName())?"未设置":model.getName());
    }
}
