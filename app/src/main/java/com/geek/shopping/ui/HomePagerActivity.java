package com.geek.shopping.ui;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.adapter.HomePagerAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.UserModel;
import com.geek.shopping.model.HealthModel;
import com.geek.shopping.model.TabModel;
import com.geek.shopping.util.ImageUtil;
import com.geek.shopping.util.PreferencesUtil;
import com.geek.shopping.util.ToastUtil;
import com.geek.shopping.view.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class HomePagerActivity extends BaseActivity {
    @BindView(R.id.header)
    public MyImageView mHeader;
    @BindView(R.id.name)
    public TextView mName;
    @BindView(R.id.viewPager)
    public ViewPager mVp;
    @BindView(R.id.indicatorLayout)
    public LinearLayout mIndicatorLayout;
    @BindView(R.id.placeLayout)
    public LinearLayout mPlaceLayout;

    private HomePagerAdapter mAdapter;
    private int currPosition;
    private int currTab;

    @BindArray(R.array.homePagerTitle)
    public String[] mSeasonTitles;
    @BindArray(R.array.homePagerDes)
    public String[] mSeasonDes;

    @BindArray(R.array.homePagerTitle1)
    public String[] mSeasonTitles1;
    @BindArray(R.array.homePagerDes1)
    public String[] mSeasonDes1;

    @BindArray(R.array.homePagerTitle2)
    public String[] mSeasonTitles2;
    @BindArray(R.array.homePagerDes2)
    public String[] mSeasonDes2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_pager;
    }

    @Override
    protected void initData() {
        initBanner();

        addTab(getData(),currTab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUser();
    }

    private void updateUser(){
        UserModel model = MyApplication.getInstance().mDbUser.getUserData(PreferencesUtil.getStringData(getApplicationContext(), ConfigUtil.KEY_PHONE));
        if (model == null)return;
        ImageUtil.loadCircleImage(
                this,model.getHeaderImg(),R.drawable.head_defaut,R.drawable.head_defaut,mHeader
        );
        mName.setText(TextUtils.isEmpty(model.getName())?"未设置":model.getName());
    }

    private void initBanner() {

        mAdapter = new HomePagerAdapter();
        mVp.setAdapter(mAdapter);
        mVp.setClipChildren(false);
        mVp.setOffscreenPageLimit(2);
        mVp.setPageMargin(10);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currPosition = i;
                initIndicatorView(mAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mVp.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void updateData(List<HealthModel> list){
        assert mAdapter != null;
        mVp.setAdapter(mAdapter);
        mAdapter.setData(list);
        initIndicatorView(list.size());
    }

    private void initIndicatorView(int size) {
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.my_indicator_bg);
            imageView.setSelected(i == currPosition);
            mIndicatorLayout.addView(imageView);
        }
    }

    public void addTab(final List<TabModel> data, int tab) {
        if (data == null || data.size() == 0 || mPlaceLayout == null) return;
        if (mPlaceLayout.getChildCount() > 0)
            mPlaceLayout.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            View v = View.inflate(this, R.layout.home_pager_place_layout, null);
            TextView name = v.findViewById(R.id.name);
            name.setText(data.get(i).getName());
            name.setSelected(i == 0);
            final int index = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currTab != index) {
                        currPosition = 0;
                        setTvChecked(index);
                        updateData(data.get(index).getData());
                    }
                }
            });
            mPlaceLayout.addView(v);
        }

        if (tab > mPlaceLayout.getChildCount() - 1) {
            tab = mPlaceLayout.getChildCount() - 1;
        }

        setTvChecked(tab);
        updateData(data.get(0).getData());
    }

    private void setTvChecked(int index) {
        currTab = index;

        for (int i = 0; i < mPlaceLayout.getChildCount(); i++) {
            mPlaceLayout.getChildAt(i).findViewById(R.id.name).setSelected(index == i);
        }
    }


    private List<TabModel> getData(){
        List<TabModel> list = new ArrayList<>();

        TabModel m1 = new TabModel();
        m1.setName("季节");
        m1.setData(getSeason());
        list.add(m1);

        TabModel m2 = new TabModel();
        m2.setName("时辰");
        m2.setData(getSeason1());
        list.add(m2);

        TabModel m3 = new TabModel();
        m3.setName("年龄");
        m3.setData(getSeason2());
        list.add(m3);

        return list;
    }

    private List<HealthModel> getSeason(){
        List<HealthModel> list = new ArrayList<>();
        for (int i=0;i<mSeasonTitles.length;i++){
            HealthModel model = new HealthModel();
            model.setTitle(mSeasonTitles[i]);
            model.setDes(mSeasonDes[i]);
            list.add(model);
        }
        return list;
    }

    private List<HealthModel> getSeason1(){
        List<HealthModel> list = new ArrayList<>();
        for (int i=0;i<mSeasonTitles1.length;i++){
            HealthModel model = new HealthModel();
            model.setTitle(mSeasonTitles1[i]);
            model.setDes(mSeasonDes1[i]);
            list.add(model);
        }
        return list;
    }

    private List<HealthModel> getSeason2(){
        List<HealthModel> list = new ArrayList<>();
        for (int i=0;i<mSeasonTitles2.length;i++){
            HealthModel model = new HealthModel();
            model.setTitle(mSeasonTitles2[i]);
            model.setDes(mSeasonDes2[i]);
            list.add(model);
        }
        return list;
    }

    public void onHeaderClick(View view) {
        Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
        startActivity(intent);
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private float MAX_SCALE = 1.0f;
        private float MIN_SCALE = 0.8f;
        private float MIN_Alpha = 0.8f;

        @Override
        public void transformPage(View view, float position) {
            if (position < -1) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) {
                float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
                float alpha = MIN_Alpha + ((1 - Math.abs(position)) * (MAX_SCALE - MIN_Alpha));
                if (position > 0) {
                    view.setTranslationX(-scaleFactor * 2);
                } else if (position < 0) {
                    view.setTranslationX(scaleFactor * 2);
                }
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(alpha);
            } else {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setAlpha(MIN_Alpha);
            }
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showShort(getApplicationContext(), "再按一次退出应用");
                mExitTime = System.currentTimeMillis();
            } else {
                exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
