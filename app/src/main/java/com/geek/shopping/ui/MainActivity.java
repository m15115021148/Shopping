package com.geek.shopping.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
    public static MainActivity mContext;
    @BindView(R.id.navigation)
    public BottomNavigationView mNavigation;
    private Fragment currentFragment;//当前视图
    private FragmentTransaction fragmentTransaction;//事务

    private IssueFragment mIssueFragment;
    private MySelfFragment mMySelfFragment;

    private String[] strs = {Manifest.permission.CAMERA};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mContext = this;
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //init view
        mIssueFragment = new IssueFragment();
        mMySelfFragment = new MySelfFragment();

        switchFragment(mIssueFragment);

        requestPermission();
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

    private void requestPermission(){
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,strs,1);
        }
    }
}
