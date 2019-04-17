package com.geek.shopping.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.geek.shopping.R;
import com.geek.shopping.log.LogUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ${chenM} on 2019/4/15.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    public int screenWidth = 0;
    public int screenHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        getScreenSize();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    /**
     * init realm
     */
    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(getString(R.string.realm_name))
                .schemaVersion(getResources().getInteger(R.integer.realm_version))
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        LogUtil.w("height:" + screenHeight + " width:" + screenWidth);
    }

}
