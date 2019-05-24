package com.geek.shopping.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.adapter.IssueAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.ui.IssueActivity;
import com.geek.shopping.ui.IssueDetailActivity;
import com.geek.shopping.view.GlideImageLoader;
import com.geek.shopping.view.recyclerview.EmptyWrapper;
import com.geek.shopping.view.recyclerview.HeaderAndFooterWrapper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发布
 */
public class IssueFragment extends BaseFragment implements IssueAdapter.OnIssueCallBack {
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private IssueAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private Banner mBanner;
    @BindView(R.id.issue)
    public TextView mIssue;

    @Override
    protected int setContentView() {
        return R.layout.fragment_issue;
    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new IssueAdapter(this);
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        initBanner();

        mIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IssueActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void startLoad() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        List<ProductModel> list = MyApplication.getInstance().mDbIssue.getUserData(ConfigUtil.USER_ID);

        if (list != null && list.size()>0){
            initBanner();

            if (mBanner != null){
                mBanner.setImages(getBannerData(list));
                mBanner.start();
            }

            mWrapper.removeFootView();
            mAdapter.setData(list);
            mWrapper.notifyDataSetChanged();
        }else {
            mWrapper.removeHeaderView();
            mAdapter.setData(new ArrayList<ProductModel>());

            setEmptyView();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null){
            mBanner.stopAutoPlay();
        }
    }

    private void initBanner(){
        mWrapper.removeHeaderView();

        View view = getLayoutInflater().inflate(R.layout.issue_header_layout,null);

        mBanner = view.findViewById(R.id.banner);

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setBannerAnimation(Transformer.ZoomIn);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mWrapper.addHeaderView(view);

        mRecyclerView.setAdapter(mWrapper);


    }

    private List<String> getBannerData(List<ProductModel> list){
        List<String> l = new ArrayList<>();
        for (ProductModel model:list){
            String[] split = model.getImg().split(";");
            if (split.length>0){
                l.add(split[0]);
            }
        }
        return l;
    }

    @Override
    public void onClickListener(int position) {
        Intent intent = new Intent(getContext(), IssueDetailActivity.class);
        intent.putExtra("ProductModel",mAdapter.getData().get(position));
        startActivity(intent);
    }

    private void setEmptyView(){
        View view = getLayoutInflater().inflate(R.layout.issue_empty_layout,null);

        TextView issue = view.findViewById(R.id.issue);

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IssueActivity.class);
                startActivity(intent);
            }
        });

        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        view.setPadding(0,500,0,0);
        mWrapper.removeFootView();
        mWrapper.addFootView(view);

        mRecyclerView.setAdapter(mWrapper);
    }
}
