package com.geek.shopping.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.geek.shopping.database.entity.ProductModel;
import com.geek.shopping.database.entity.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBIssue {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public static final String ISSUEID = "issueId";
    public static final String UASERID = "userId";
    public static final String TITLE = "title";
    public static final String TIME = "time";
    public static final String PRODUCTNAME = "productName";
    public static final String MONEY = "money";
    public static final String DETAIL = "detail";
    public static final String IMG = "img";

    public DBIssue(Context context) {
        dbHelper = DBHelper.getNewInstanceDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    /**
     * 插入数据
     */
    public void insert(ProductModel model) {
        ContentValues values = new ContentValues();
        values.put(UASERID,model.getUserId());
        values.put(TIME, new Date().getTime());
        values.put(TITLE, model.getTitle());
        values.put(PRODUCTNAME, model.getProductName());
        values.put(MONEY, String.valueOf(model.getMoney()));
        values.put(DETAIL, model.getDetail());
        values.put(IMG, model.getImg());
        db.insert(DBHelper.ISSUE, null, values);
        Log.i("result", "插入成功");
    }

    /**
     * 删除表
     */
    public void delTab() {
        db.execSQL("delete from " + DBHelper.ISSUE + ";");
    }

    /**
     * 删除一条记录
     *
     * @param issueId
     *
     */
    public void delOneData(int issueId) {
        String sql = "delete from " + DBHelper.ISSUE + " where issueId=" + issueId + ";";
        Log.e("result", "sql:" + sql);
        db.execSQL(sql);
    }

    /**
     * 查询数据
     * @return
     */
    public List<ProductModel> getUserData(int userId) {
        List<ProductModel> list = new ArrayList<>();

        String sql = "select * from " + DBHelper.ISSUE + " where userId=" + userId + ";";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int issueId = cursor.getInt(cursor.getColumnIndex(ISSUEID));
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            long time = cursor.getLong(cursor.getColumnIndex(TIME));
            String productName = cursor.getString(cursor.getColumnIndex(PRODUCTNAME));
            String money = cursor.getString(cursor.getColumnIndex(MONEY));
            String detail = cursor.getString(cursor.getColumnIndex(DETAIL));
            String img = cursor.getString(cursor.getColumnIndex(IMG));

            ProductModel model = new ProductModel();
            model.setIssueId(issueId);
            model.setUserId(userId);
            model.setTitle(title);
            model.setCreateTime(time);
            model.setProductName(productName);
            model.setMoney(Double.parseDouble(money));
            model.setDetail(detail);
            model.setImg(img);

            list.add(model);

        }


        return list;
    }
}
