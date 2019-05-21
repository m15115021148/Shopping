package com.geek.shopping.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class IssueDetailActivity extends BaseActivity {
    @BindView(R.id.back)
    public LinearLayout mBack;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.banner)
    public Banner mBanner;
    @BindView(R.id.buy)
    public TextView mBuy;
    @BindView(R.id.name)
    public TextView mName;

    private ProductModel mModel;

    private List<String> mBannerData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_detail;
    }

    @Override
    protected void initData() {
        mTitle.setText("商品详情");
        mBack.setOnClickListener(this);
        mBuy.setOnClickListener(this);

        mModel = (ProductModel) getIntent().getSerializableExtra("ProductModel");

        if (mModel == null){
            finish();
            return;
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBannerData.clear();
        mBannerData = Arrays.asList(mModel.getImg().split(";"));

        mName.setText(String.format("%s %s",mModel.getProductName(),mModel.getDetail()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBanner.setImages(mBannerData);
        mBanner.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            finish();
        }
        if (v == mBuy){

        }
    }
}
