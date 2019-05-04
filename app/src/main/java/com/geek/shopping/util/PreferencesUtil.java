package com.geek.shopping.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private static final String TAG = PreferencesUtil.class.getSimpleName();
    private static final String SP_LOGIN_PRIVATE = "PreferencesUtil_private_shopping";

    public static void isFirst(Context context, String key, boolean isFirst) {
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, isFirst).apply();
    }

    public static boolean getFirst(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setStringData(Context context, String key, String values){
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        sp.edit().putString(key,values).apply();
    }

    public static String getStringData(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void setIntegerData(Context context, String key, int values){
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key,values).apply();
    }

    public static int getIntegerData(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
                Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }

}
