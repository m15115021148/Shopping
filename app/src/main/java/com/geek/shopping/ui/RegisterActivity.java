package com.geek.shopping.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.database.RealmUtil;
import com.geek.shopping.database.entity.UserModel;
import com.geek.shopping.util.ToastUtil;

import java.util.Date;

import butterknife.BindView;
import io.realm.Realm;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;
    @BindView(R.id.phone)
    public EditText mPhone;
    @BindView(R.id.code)
    public EditText mCode;
    @BindView(R.id.password)
    public EditText mPassword;
    @BindView(R.id.register)
    public TextView mRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        mBack.setOnClickListener(this);
        mTitle.setText(getString(R.string.login_register));
        mRegister.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            finish();
        }
        if (v == mRegister){
            if (TextUtils.isEmpty(mPhone.getText().toString())){
                ToastUtil.showShort(getApplicationContext(),"手机号码不能为空");
                return;
            }
            if (TextUtils.isEmpty(mCode.getText().toString())){
                ToastUtil.showShort(getApplicationContext(),"验证码不能为空");
                return;
            }
            if (TextUtils.isEmpty(mPassword.getText().toString())){
                ToastUtil.showShort(getApplicationContext(),"密码不能为空");
                return;
            }
            saveUserInfo(mPhone.getText().toString(), MyApplication.getMD5(mPassword.getText().toString()));
        }

    }

    private void saveUserInfo(String phone,String password){
        UserModel model = MyApplication.getInstance().mDbUser.getUserData(phone);

        if (model == null){
            UserModel m = new UserModel();
            m.setPhone(phone);
            m.setPassword(password);
            m.setHeaderImg("");
            m.setSex(0);
            m.setName("");
            m.setAddress("");
            m.setTime(new Date().getTime());

            MyApplication.getInstance().mDbUser.insert(m);

            ToastUtil.showShort(getApplicationContext(),"注册成功");
            finish();
        }else {
            ToastUtil.showShort(getApplicationContext(),"账号已存在");
        }
    }
}
