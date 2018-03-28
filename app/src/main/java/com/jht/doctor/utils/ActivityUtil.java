package com.jht.doctor.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by zhaoyun on 2018/1/13.
 */
public class ActivityUtil {

    public static boolean installApk(Activity receiver , File apkFile , String authority){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(receiver , authority , apkFile);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            receiver.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean installApk(Fragment receiver , File apkFile , String authority){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(receiver.getContext() , authority , apkFile);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            receiver.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean installApk(android.support.v4.app.Fragment receiver , File apkFile , String authority){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(receiver.getContext() , authority , apkFile);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            receiver.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //打开照相机
    public static boolean openCamera(Activity activity, File file, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = UriUtil.getUri(activity, file);
                //添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            activity.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //打开相册
    public static boolean openAlbum(Activity receiver, String mimeType, int requestCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mimeType);
            receiver.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
