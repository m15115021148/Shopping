package com.geek.shopping.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.geek.shopping.R;
import com.geek.shopping.adapter.IssueAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.view.BannerIndicator;
import com.geek.shopping.view.SmoothLinearLayoutManager;

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
    public RecyclerView mBanner;
    @BindView(R.id.indicator)
    public BannerIndicator mBannerIndicator;

    private IssueAdapter mBannerAdapter;
    private SmoothLinearLayoutManager layoutManager;

    @Override
    protected int setContentView() {
        return R.layout.fragment_issue;
    }

    @Override
    protected void initData() {
        initBanner();
    }

    @Override
    protected void startLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        List<ProductModel> list = MyApplication.getInstance().mDbIssue.getUserData(ConfigUtil.USER_ID);

        if (list != null && list.size()>0){
            mBannerAdapter.setData(list);
            mBannerIndicator.setNumber(list.size());
        }
    }

    private void initBanner(){
        mBannerAdapter = new IssueAdapter();

        layoutManager = new SmoothLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBanner.setLayoutManager(layoutManager);
        mBanner.setHasFixedSize(true);
        mBanner.setAdapter(mBannerAdapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBanner);

        mBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = layoutManager.findFirstVisibleItemPosition() % mBannerAdapter.getData().size();
                    mBannerIndicator.setPosition(i);
                }
            }
        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mBanner.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);

    }

}
