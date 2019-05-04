package com.geek.shopping.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}