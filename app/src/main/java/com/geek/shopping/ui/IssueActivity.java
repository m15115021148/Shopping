package com.geek.shopping.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.adapter.ImageAdapter;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.log.LogUtil;
import com.geek.shopping.model.ImageModel;
import com.geek.shopping.util.DialogUtil;
import com.geek.shopping.util.PictureUtil;
import com.geek.shopping.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class IssueActivity extends BaseActivity implements ImageAdapter.OnImageCallBack {
    private IssueActivity context;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;

    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private boolean showAddIcon = true;
    private Dialog dialog;
    private int type;//0第一张，1第二张，2第三张图

    private List<ImageModel> mList = new ArrayList<>();

    @BindView(R.id.rightLayout)
    public LinearLayout mRightLayout;

    @BindView(R.id.issueTitle)
    public EditText mIssueTitle;
    @BindView(R.id.issueProductName)
    public EditText mIssueName;
    @BindView(R.id.issueMoney)
    public EditText mIssueMoney;
    @BindView(R.id.issueDetail)
    public EditText mIssueDetail;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue;
    }

    @Override
    protected void initData() {
        context = this;
        mBack.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
        mTitle.setText("发布");
        initRegisterView();

        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) finish();
        if (v == mRightLayout) {
            if (TextUtils.isEmpty(mIssueTitle.getText().toString())) {
                ToastUtil.showShort(context, "请输入标题");
                return;
            }
            if (TextUtils.isEmpty(mIssueName.getText().toString())) {
                ToastUtil.showShort(context, "请输入产品名称");
                return;
            }
            if (TextUtils.isEmpty(mIssueMoney.getText().toString())) {
                ToastUtil.showShort(context, "请输入金额");
                return;
            }
            if (TextUtils.isEmpty(mIssueDetail.getText().toString())) {
                ToastUtil.showShort(context, "请输入简介");
                return;
            }
            if (mList.size() <= 0) {
                ToastUtil.showShort(context, "请上传图片");
                return;
            }

            ProductModel model = new ProductModel();
            model.setUserId(ConfigUtil.USER_ID);
            model.setTitle(mIssueTitle.getText().toString());
            model.setProductName(mIssueName.getText().toString());
            model.setMoney(Double.parseDouble(mIssueMoney.getText().toString()));
            model.setDetail(mIssueDetail.getText().toString());
            model.setImg(getImg());

            MyApplication.getInstance().mDbIssue.insert(model);

            ToastUtil.showShort(context, "发布成功");
            finish();
        }
    }

    private void initRegisterView() {
        TextView tv = new TextView(this);
        tv.setText("确定");
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.white));
        mRightLayout.addView(tv);
    }

    private String getImg() {
        String str = "";
        try {
            for (ImageModel model : mList) {
                str = str + model.getFile().getPath() + ";";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        mList.add(new ImageModel());
        mAdapter.setData(mList);
    }

    @Override
    public void onClickListener(int position) {
        if (position == mList.size() - 1 && showAddIcon) {
            dialogSHow();
        } else {

        }
    }

    private void dialogSHow() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            onImageBack();
            return;
        }
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

    private void onImageBack() {
        if (mList.get(mList.size() - 1).getFile() != null) {
            mList.add(new ImageModel());
        }
        mList.remove(type);
        mAdapter.setData(mList);
        showAddIcon = true;
    }


    private void compressResult(File file) {
        ImageModel model = new ImageModel();
        model.setFile(file);
        mList.add(mList.size() - 1, model);
        if (mList.size() > 3) {
            mList.remove(mList.size() - 1);
            showAddIcon = false;
        }
        mAdapter.setData(mList);
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
                        compressResult(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();    //启动压缩

    }
}
