package com.geek.shopping.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.UserModel;
import com.geek.shopping.log.LogUtil;
import com.geek.shopping.util.ActivityUtil;
import com.geek.shopping.util.DialogUtil;
import com.geek.shopping.util.FileUriPermissionCompat;
import com.geek.shopping.util.ImageRotateUtil;
import com.geek.shopping.util.ImageUtil;
import com.geek.shopping.util.PictureUtil;
import com.geek.shopping.util.PreferencesUtil;
import com.geek.shopping.util.ToastUtil;
import com.geek.shopping.view.MyImageView;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AccountActivity extends BaseActivity {
    private AccountActivity context;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;

    @BindView(R.id.header)
    public MyImageView mHeader;

    @BindView(R.id.nickNameLayout)
    public RelativeLayout mNickNameLayout;
    @BindView(R.id.nickName)
    public TextView mNickName;
    @BindView(R.id.sexLayout)
    public RelativeLayout mSexLayout;
    @BindView(R.id.sex)
    public TextView mSex;
    private String telPhone;
    private Dialog dialog;

    int rotateDegree;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initData() {
        context = this;
        mTitle.setText("个人中心");
        mBack.setOnClickListener(this);
        mNickNameLayout.setOnClickListener(this);
        mSexLayout.setOnClickListener(this);
        mHeader.setOnClickListener(this);

        telPhone = PreferencesUtil.getStringData(getApplicationContext(), ConfigUtil.KEY_PHONE);

        updateUserData();
    }

    private void updateUserData(){
        if (TextUtils.isEmpty(telPhone))return;
        UserModel model = MyApplication.getInstance().mDbUser.getUserData(telPhone);
        if (model != null){
            mNickName.setText(model.getName());
            mSex.setText(model.getSex()==0?"男":"女");
            ImageUtil.loadCircleImage(
                    this,model.getHeaderImg(),R.drawable.head_defaut,R.drawable.head_defaut,mHeader
            );
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) finish();
        if (v == mHeader){
            dialogHeaderSHow();
        }
        if (v == mNickNameLayout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = getLayoutInflater().inflate(R.layout.account_input_layout,null);
            final EditText name = view.findViewById(R.id.name);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (TextUtils.isEmpty(name.getText().toString())){
                        ToastUtil.showShort(getApplicationContext(),"昵称不能为空");
                        return;
                    }
                    mNickName.setText(name.getText().toString());
                    MyApplication.getInstance().mDbUser.updateUserNickName(telPhone,name.getText().toString());
                }
            });
            builder.setNegativeButton("取消",null);

            builder.setView(view);
            builder.create().show();
        }
        if (v == mSexLayout){
            dialogSexSHow();
        }
    }

    private void dialogHeaderSHow() {
        dialog = DialogUtil.selectPic(this, null, null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_top) {
                    PictureUtil.getImageFromCamera(context);
                } else if (v.getId() == R.id.btn_mid) {
                    PictureUtil.getImageFromAlbum(context);
                } else if (v.getId() == R.id.btn_bottom) {

                }
                dialog.cancel();
            }
        });
    }

    private void dialogSexSHow() {
        dialog = DialogUtil.selectPic(this, "男", "女", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_top) {
                    mSex.setText("男");
                    MyApplication.getInstance().mDbUser.updateUserSex(telPhone,0);
                } else if (v.getId() == R.id.btn_mid) {
                    mSex.setText("女");
                    MyApplication.getInstance().mDbUser.updateUserSex(telPhone,1);
                } else if (v.getId() == R.id.btn_bottom) {

                }
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //data不为空时为选取照片的回调
        if (data != null) {
            if (requestCode == PictureUtil.REQUEST_CODE_PICK_IMAGE && resultCode == -1) {
                Uri uri = data.getData();
                LogUtil.e(uri.getPath());
                if (uri != null) {
                    String path = PictureUtil.getPath(context, uri);

                    Uri dealedUri = Uri.fromFile(new File(path));
                    setPic(dealedUri);
                }
            }
        } else {
            //data为空时为拍摄照片的回调
            if (PictureUtil.capturePath != null && requestCode == PictureUtil.REQUEST_CODE_CAPTURE_CAMEIA && resultCode == -1) {
                Uri uri = Uri.fromFile(new File(PictureUtil.capturePath));
                if (uri != null)
                    setPic(uri);
            }
        }
    }

    private void setPic(Uri path) {
        File oldFile = new File(path.getPath());

        Luban.with(this)
                .load(oldFile)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(PictureUtil.getTempFilePath())    // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        updateHeader(file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();    //启动压缩

    }

    private void updateHeader(String path){
        ImageUtil.loadCircleImage(
                this,path,R.drawable.head_defaut,R.drawable.head_defaut,mHeader
        );
        MyApplication.getInstance().mDbUser.updateUserHeader(telPhone,path);
    }

}
