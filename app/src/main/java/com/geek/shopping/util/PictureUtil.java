package com.geek.shopping.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.geek.shopping.config.ConfigUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class PictureUtil {

    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 0;
    public static final int REQUEST_CODE_PICK_IMAGE = 1;

    public static String capturePath;
    public static Uri sSaveUri = null;

    public static void getImageFromCamera(Activity activity) {
        try {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent getImageByCamera = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                createImagePath(activity);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){


                    Uri contentUri = FileProvider.getUriForFile(
                            activity.getApplicationContext(), activity.getApplication().getPackageName()+".FileProvider", new File(capturePath));



                 /*   Uri contentUri = FileProvider.getUriForFile(
                            context, "com.sensology.all.FileProvider", new File(capturePath));*/

    //                Uri contentUri = FileUriPermissionCompat.adaptUriAndGrantPermission(context,getImageByCamera,new File(capturePath));
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);
                }else{
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(capturePath)));
                }
                activity.startActivityForResult(getImageByCamera,
                        REQUEST_CODE_CAPTURE_CAMEIA);
            } else {
                Toast.makeText(activity, "无法读取到SD卡中的图片", Toast.LENGTH_LONG).show();
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public static void getImageFromAlbum(Activity activity) {
        createImagePath(activity);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_PICK_IMAGE);

    }

    private static void createImagePath(Activity activity){
        String dir = Environment.getExternalStorageDirectory().toString()
                + "/Shopping/" + PreferencesUtil.getStringData(activity, ConfigUtil.KEY_PHONE) + "/issue";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdirs();
        }
        capturePath = dir + "/"+ System.currentTimeMillis()+".jpg";


        sSaveUri = Uri.fromFile(new File(capturePath));

    }

    private static int getZoomScale(int mDisplayWidth,
                                    int mDisplayHeight) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(capturePath, options);
        if (options.outWidth > options.outHeight
                && options.outWidth > mDisplayWidth) {
            scale = (int) (options.outWidth / mDisplayWidth);
        } else if (options.outWidth < options.outHeight
                && options.outHeight > mDisplayHeight) {
            scale = (int) (options.outHeight / mDisplayHeight);
        }
        return scale;
    }

//    public static void deleteBitmap(String picName) {
//        try {
//            File f = new File(Environment.getExternalStorageDirectory().toString()
//                    + "/Sensology/" + SharedPrefUtil.getTel() + "/", picName);
//            if (f.exists()) {
//                f.delete();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    public static String getTempFilePath(){
//        String dir = Environment.getExternalStorageDirectory().toString()
//                + "/Sensology/" + SharedPrefUtil.getTel() + "/IconCache";
//        File f = new File(dir);
//        if(!f.exists()){
//            f.mkdirs();
//        }
//
//        return f.getPath();
//
////        capturePath = dir + "/avatar.jpg";
////        capturePath = dir + "/"+System.currentTimeMillis()+".jpg";
////        sSaveUri = Uri.fromFile(new File(capturePath));
//    }


















    //通用的从uri中获取路径的方法, 兼容以上说到的2个shceme
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }


    public static long getId(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getIdColumn(context, contentUri, selection, selectionArgs);
            }
        } else {
            return getIdColumn(context, uri, null, null);
        }
        return 0;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }
    public static long getIdColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_id";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getLong(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }






    public static String getTempFilePath(){
        String dir = Environment.getExternalStorageDirectory().toString()
                + "/Sensology/" + ConfigUtil.KEY_PHONE + "/IconCache";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdirs();
        }

        return f.getPath();

//        capturePath = dir + "/avatar.jpg";
//        capturePath = dir + "/"+System.currentTimeMillis()+".jpg";
//        sSaveUri = Uri.fromFile(new File(capturePath));
    }

}