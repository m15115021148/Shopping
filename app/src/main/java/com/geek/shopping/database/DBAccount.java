package com.geek.shopping.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.geek.shopping.database.entity.UserModel;


public class DBAccount {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public static final String USERID = "userId";
    public static final String PHONE = "phone";
    public static final String TIME = "time";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String SEX = "sex";

    public DBAccount(Context context) {
        dbHelper = DBHelper.getNewInstanceDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    /**
     * 插入数据
     */
    public void insert(UserModel model) {
        ContentValues values = new ContentValues();
        values.put(PHONE, model.getPhone());
        values.put(TIME, model.getTime());
        values.put(PASSWORD, model.getPassword());
        values.put(ADDRESS, model.getAddress());
        values.put(NAME, model.getTime());
        values.put(SEX, model.getSex());
        db.insert(DBHelper.USER, null, values);
        Log.i("result", "插入成功");
    }

    /**
     * 删除表
     */
    public void delTab() {
        db.execSQL("delete from " + DBHelper.USER + ";");
    }

    /**
     * 删除一条记录
     *
     * @param phone
     */
    public void delOneUser(String phone) {
        String sql = "delete from " + DBHelper.USER + " where phone=" + phone + ";";
        Log.e("result", "sql:" + sql);
        db.execSQL(sql);
    }

    /**
     * 查询数据
     * @return
     */
    public UserModel getUserData(String telPhone) {

        String sql = "select * from " + DBHelper.USER + " where phone=" + telPhone + ";";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(USERID));
            String phone = cursor.getString(cursor.getColumnIndex(PHONE));
            long time = cursor.getLong(cursor.getColumnIndex(TIME));
            String password = cursor.getString(cursor.getColumnIndex(PASSWORD));
            String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            int sex = cursor.getInt(cursor.getColumnIndex(SEX));

            UserModel model = new UserModel();

            model.setUserId(id);
            model.setPhone(phone);
            model.setTime(time);
            model.setPassword(password);
            model.setAddress(address);
            model.setName(name);
            model.setSex(sex);

            return model;

        }


        return null;
    }
}
