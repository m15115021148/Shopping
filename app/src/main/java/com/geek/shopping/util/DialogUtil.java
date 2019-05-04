package com.geek.shopping.util;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.geek.shopping.R;


public class DialogUtil {

    public static Dialog selectPic(final Activity context, String top, String mid, String bottom, View.OnClickListener listener) {
        final Dialog dialog2 = new Dialog(context, R.style.myDialog);
        dialog2.setContentView(R.layout.dialog_itme_choose_dial);

        LinearLayout ll = (LinearLayout) dialog2.findViewById(R.id.ll_dialog);
        ll.setLayoutParams(new FrameLayout.LayoutParams(ActivityUtil.getScreenWidthMetrics((Activity) context)
                , FrameLayout.LayoutParams.WRAP_CONTENT));


        Button btn_top = (Button) dialog2.findViewById(R.id.btn_top);
        Button btn_mid = (Button) dialog2.findViewById(R.id.btn_mid);
        Button btn_bottom = (Button) dialog2.findViewById(R.id.btn_bottom);

        if (!TextUtils.isEmpty(top)) btn_top.setText(top);
        if (!TextUtils.isEmpty(mid)) btn_mid.setText(mid);
        if (!TextUtils.isEmpty(bottom)) btn_bottom.setText(bottom);

        btn_top.setOnClickListener(listener);
        btn_mid.setOnClickListener(listener);
        btn_bottom.setOnClickListener(listener);
        Window window = dialog2.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialogFromBottom);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.x = 0;
            lp.y = -20;
            window.setAttributes(lp);
        }
        dialog2.setCanceledOnTouchOutside(true);// 设置点击其他地方能消失
        dialog2.show();
        return dialog2;
    }
}
