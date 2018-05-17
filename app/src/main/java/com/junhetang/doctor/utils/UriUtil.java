package com.junhetang.doctor.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by zhaoyun on 2018/2/4.
 */
public class UriUtil {

    /**
     * 根据系统版本获取不同的uri
     *
     * @param context
     * @param file
     * @return Uri
     */
    public static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 以上需要 authorities 属性值
            return FileProvider.getUriForFile(context, UriUtil.getFileProviderAuth(context), file);
        } else {
            // > 7.0
            return Uri.fromFile(file);
        }
    }

    /**
     * 7.0以上以File路径来生成Uri时需要配置
     *
     * @param context
     * @return String
     */
    public static String getFileProviderAuth(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * 头像图片临时文件的文件名
     *
     * @param context
     * @return String
     */
    public static String headerFileName(Context context) {
        return context.getPackageName() + "-header-" + SystemClock.currentThreadTimeMillis() + ".jpg";
    }

    /**
     * 反馈图片临时文件的文件名
     *
     * @param context
     * @return
     */
    public static String feedbackFileName(Context context) {
        return context.getPackageName() + "-feedback-" + SystemClock.currentThreadTimeMillis() + ".jpg";
    }

    /**
     * android 4.4 获取 uri 真实路径
     *
     * @param uri
     * @return String /sdcard/0/xx.jpg
     */
    public static String getRealFilePath(Context context, Uri uri) {
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
