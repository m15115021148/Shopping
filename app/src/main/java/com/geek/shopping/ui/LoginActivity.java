package com.geek.shopping.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geek.shopping.R;
import com.geek.shopping.application.MyApplication;
import com.geek.shopping.config.ConfigUtil;
import com.geek.shopping.database.RealmUtil;
import com.geek.shopping.database.entity.UserModel;
import com.geek.shopping.util.PreferencesUtil;
import com.geek.shopping.util.ToastUtil;

import butterknife.BindView;
import io.realm.Realm;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.email)
    public AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    public EditText mPasswordView;
    @BindView(R.id.login_form)
    public View mLoginFormView;
    @BindView(R.id.email_sign_in_button)
    public TextView mEmailSignInButton;
    @BindView(R.id.title)
    public TextView mTitle;
    @BindView(R.id.back)
    public LinearLayout mBack;
    @BindView(R.id.rightLayout)
    public LinearLayout mRightLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        mTitle.setText(getString(R.string.login_title));
        mBack.setVisibility(View.GONE);
        mRightLayout.setOnClickListener(this);
        initRegisterView();
        populateAutoComplete();

        String tel = PreferencesUtil.getStringData(getApplicationContext(), ConfigUtil.KEY_PHONE);
        mEmailView.setText(tel);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initRegisterView(){
        TextView tv = new TextView(this);
        tv.setText(getString(R.string.login_register));
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.white));
        mRightLayout.addView(tv);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, 1);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, 1);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String data = getUserInfo(email, password);

            ToastUtil.showShort(getApplicationContext(),data);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    @Override
    public void onClick(View v) {
        if (v == mRightLayout){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
    }

    private String getUserInfo(String phone,String password){
        UserModel model = MyApplication.getInstance().mDbUser.getUserData(phone);

        if (model == null){
            return "账号不存在";
        }
        if (!model.getPassword().equals(MyApplication.getMD5(password))){
            return "密码不正确";
        }
        ConfigUtil.USER_ID = model.getUserId();
        PreferencesUtil.setStringData(getApplicationContext(), ConfigUtil.KEY_PHONE,phone);
        PreferencesUtil.setStringData(getApplicationContext(),ConfigUtil.KEY_PASSWORD,password);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        return "登陆成功";
    }
}

