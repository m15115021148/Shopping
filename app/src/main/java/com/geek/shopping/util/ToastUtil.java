package com.geek.shopping.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;

    public static void showShort(Context context, CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showShort(Context context, int resId) {
        if (context != null) {
            showShort(context, context.getString(resId));
        }
    }

    public static void showLong(Context context, CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(Context context, int resId) {
        if (context != null) {
            showLong(context, context.getString(resId));
        }
    }

    public static void showCustomDuration(Context context, CharSequence message, int duration) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showCustomDuration(Context context, int resId, int duration) {
        if (context != null) {
            showCustomDuration(context, context.getString(resId), duration);
        }
    }
}
