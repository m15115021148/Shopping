package com.geek.shopping.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.geek.shopping.R;
import com.geek.shopping.fragment.DataFragment;
import com.geek.shopping.fragment.IssueFragment;
import com.geek.shopping.fragment.MySelfFragment;
import com.geek.shopping.util.ToastUtil;

import butterknife.BindView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.navigation)
    public BottomNavigationView mNavigation;
    private Fragment currentFragment;//当前视图
    private FragmentTransaction fragmentTransaction;//事务

    private IssueFragment mIssueFragment;
    private MySelfFragment mMySelfFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //init view
        mIssueFragment = new IssueFragment();
        mMySelfFragment = new MySelfFragment();

        switchFragment(mIssueFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(mIssueFragment);
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(mMySelfFragment);
                    return true;
            }
            return false;
        }
    };


    private void initFragmentTransaction() {
        if (fragmentTransaction == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        }
    }

    public void switchFragment(Fragment to) {
        initFragmentTransaction();
        if (currentFragment != to) {
            if (currentFragment == null) {
                fragmentTransaction.add(R.id.container, to).commit();
                currentFragment = to;
                fragmentTransaction = null;
                return;

            }
            if (!to.isAdded()) {
                fragmentTransaction.hide(currentFragment).add(R.id.container, to).commitAllowingStateLoss();
            } else {

                fragmentTransaction.hide(currentFragment).show(to).commitAllowingStateLoss();
            }
        }
        currentFragment = to;
        fragmentTransaction = null;
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
