package com.geek.shopping.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.geek.shopping.R;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.util.PreferencesUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements Runnable{
    @BindView(R.id.img)
    public ImageView mIv;
    private Bitmap bitmap;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        mIv.setImageBitmap(bitmap);

        mHandler.postDelayed(this,3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void run() {
        jump();
    }

    private void jump(){
        boolean first = PreferencesUtil.getFirst(this, ConfigUtil.KEY_LOGIN_FIRST);
        if (!first){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this,HomePagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
