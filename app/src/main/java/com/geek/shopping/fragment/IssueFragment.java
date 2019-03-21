package com.geek.shopping.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import com.geek.shopping.R;
import com.geek.shopping.util.ActivityUtil;
import com.geek.shopping.util.SystemBarTintManager;

import butterknife.BindView;

/**
 * 发布
 */
public class IssueFragment extends BaseFragment{
    @BindView(R.id.app_bar)
    public AppBarLayout mAppBarLayout;

    @Override
    protected int setContentView() {
        return R.layout.fragment_issue;
    }

    @Override
    protected void initData() {
//        setStatusBarColor(getResources().getColor(R.color.transparent));
//        setStatusBarSpace();
//        XStatusBarHelper.immersiveStatusBar(context, 0);
    }

    @Override
    protected void startLoad() {

    }

    private void setStatusBarSpace() {
        int height = new SystemBarTintManager(getActivity()).getConfig().getStatusBarHeight();
        int screenWidth = ActivityUtil.getScreenWidthMetrics(getActivity());
        int setHeight = height * 1080 / screenWidth;
        mAppBarLayout.setPadding(0, setHeight, 0, 0);
    }

}
