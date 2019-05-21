package com.geek.shopping.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.geek.shopping.R;
import com.geek.shopping.adapter.IssueAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.view.BannerIndicator;
import com.geek.shopping.view.GlideImageLoader;
import com.geek.shopping.view.SmoothLinearLayoutManager;
import com.geek.shopping.view.recyclerview.HeaderAndFooterWrapper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 发布
 */
public class IssueFragment extends BaseFragment{
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private IssueAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private Banner mBanner;


    @Override
    protected int setContentView() {
        return R.layout.fragment_issue;
    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new IssueAdapter();
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        initBanner();
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

        if (mBanner != null){
            mBanner.setImages(getBannerData(list));
            mBanner.start();
        }
        mAdapter.setData(list);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null){
            mBanner.stopAutoPlay();
        }
    }

    private void initBanner(){

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

}
