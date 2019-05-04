package com.geek.shopping.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class ImageUtil {

    /**
     * 从网络加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, int placeholder, int error, ImageView imageview) {
        try {
            Glide.with(context).load(url).crossFade().placeholder(placeholder).error(error).transform(new GlideCircleTransform(context)).into(imageview);
//            Glide.with(context).load(url).crossFade().error(error).transform(new GlideCircleTransform(context)).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地加载圆形图片
     */
    public static void loadNativeCircleImage(Context context, Integer drawable, ImageView imageview) {
        try {
            Glide.with(context).load(drawable).crossFade().placeholder(drawable).error(drawable).transform(new GlideCircleTransform(context)).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地加载普通图片
     */
    public static void loadNativeCommonImage(Context context, Integer drawable, ImageView imageview) {
        try {
            Glide.with(context).load(drawable).crossFade().placeholder(drawable).error(drawable).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadNativeImage(Context context, Integer drawable, ImageView imageview) {
        try {
            Glide.with(context).load(drawable).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从网络加载普通图片
     * 显示加载中图片
     */
    public static void loadImage(Context context, String url, int placeholder, int error, ImageView imageview) {
        try {
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().placeholder(placeholder).error(error).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从网络加载普通图片
     * 显示加载中图片
     */
    public static void loadImage(Context context, String url, int error, ImageView imageview) {
        try {
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().error(error).into(imageview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片缓存,此处只是为了了解glide特性，不可直接调用
     */
   /* public static void cleanCatch()
    {

        Glide.get(this).clearDiskCache();
        Glide.get(this).clearMemory();//clearDiskCache();有效，不过不能在UI线程里跑，得另开一个线程。。。。。Glide.get(this).clearMemory();只能在主线程里跑
    }*/
    public static void loadCircleImage(Context context, String url, int placeholder, int error, int width, int height, ImageView imageView) {
        Glide.with(context).
                load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(placeholder)
                .error(error)
                .override(width, height)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    public static void loadImage(Context context, String url, int placeholder, int error, int width, int height, ImageView imageView) {
        Glide.with(context).
                load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(placeholder)
                .error(error)
                .override(width, height)
                .dontTransform()
                .into(imageView);
    }

    public static void loadImageGrayTransform(Context context, String url, int placeholder, int error, int width, int height, ImageView imageView) {
        Glide.with(context).
                load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new GrayscaleTransformation(context))
                .crossFade()
                .placeholder(placeholder)
                .error(error)
                .override(width, height)
                .into(imageView);
    }

    //加载毛玻璃图片
    public static void loadBlurImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new BlurTransformation(context, 15))
                .into(imageView);
    }

    //加载毛玻璃图片
    public static void loadBlurImage(Context context, int resourceId, ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .bitmapTransform(new BlurTransformation(context, 15))
                .into(imageView);
    }

    /*第二个、第三个参数是px，使用时看看是不是需要转换成dp*/
    public static Bitmap getRoundCorner(float r, float g, float b) {
        int width = 100;
        int height = 100;
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        output.eraseColor(Color.rgb(r, g, b));
        return createCircleBitmap(output);
    }

    private static Bitmap createCircleBitmap(Bitmap resource) {
        //获取图片的宽度
        int width = resource.getWidth();
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //创建一个与原bitmap一样宽度的正方形bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以该bitmap为低创建一块画布
        Canvas canvas = new Canvas(circleBitmap);
        //以（width/2, width/2）为圆心，width/2为半径画一个圆
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        //设置画笔为取交集模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪图片
        canvas.drawBitmap(resource, 0, 0, paint);
        return circleBitmap;
    }


    /**
     * @param rawBitmap 原来的Bitmap
     * @param row       切成几行
     * @param column    切成几列
     * @return
     */
    public static List<Bitmap> splitImage(Bitmap rawBitmap, int row, int column){
        List<Bitmap> list = new ArrayList<>(row*column);
        int rawBitmapWidth=rawBitmap.getWidth();
        int rawBitmapHeight=rawBitmap.getHeight();
        int perPartWidth=rawBitmapWidth/column;
        int perPartHeight=rawBitmapHeight/row;
        Bitmap perBitmap=null;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int x=j*perPartWidth;
                int y=i*perPartHeight;
                perBitmap= Bitmap.createBitmap(rawBitmap, x, y, perPartWidth, perPartHeight);
                list.add(perBitmap);
            }
        }
        System.out.println("size="+list.size());
        return list;
    }

    public static Bitmap addBitmapPoint(Bitmap mBitmap, Bitmap point, int value) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

//        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        //设置画笔为取交集模式
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪图片
        canvas.drawBitmap(mBitmap, 0, 0, paint);

//        //获取原始图片与水印图片的宽与高
//        int mBitmapWidth = mBitmap.getWidth();
//        int mBitmapHeight = mBitmap.getHeight();
//        Bitmap mNewBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
//        Canvas mCanvas = new Canvas(mNewBitmap);
//        //向位图中开始画入MBitmap原始图片
//        mCanvas.drawBitmap(mBitmap,0,0,null);
//
//
//        Paint mPaint = new Paint();
//        mPaint.setColor(Color.RED);
//        mPaint.setTextSize(20);
//
////        mCanvas.drawBitmap(point,0,0,new Paint());
//
//        //水印的位置坐标
//        mCanvas.drawText("test", (mBitmapWidth * 1) / 10,(mBitmapHeight*14)/15,mPaint);
//        mCanvas.save(Canvas.ALL_SAVE_FLAG);
//        mCanvas.restore();

        return circleBitmap;
    }

}
