package com.junhetang.doctor.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;

public class FileUtil {

    public static long MAX_UPLOAD_SIZE = 1 * 1024 * 1024;

    /**
     * 压缩文件到指定大小以下
     *
     * @param file
     * @param specifiedSize
     * @return Observable<byte       [       ]>
     */
    public static Observable<byte[]> zipImage(File file, long specifiedSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return Observable
                .just(out)
                .map(byteArrayOutputStream -> {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    while (out.toByteArray().length > specifiedSize) {
                        out.reset();
                        quality -= 10;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    }
                    bitmap.recycle();
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bytes;
                });
    }

    /**
     * 压缩文件到指定大小以下
     *
     * @param file
     * @param maxSize
     * @return byte[]
     */
    public static byte[] zipImageToSize(File file, long maxSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        while (out.toByteArray().length > maxSize) {
            out.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        }
        bitmap.recycle();
        byte[] bytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String zipImageFile(File file, long maxSize) {
        byte[] fileByte = zipImageToSize(file, maxSize);
        //系统相册目录
        String peicurePath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_PICTURES
                + File.separator;
        String fileName = "jhtzip_" + file.getName();
        byte2File(fileByte, peicurePath, fileName);
        return peicurePath + fileName;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 复制文件到指定目录
     *
     * @param is
     * @param dirPath
     * @param fileName
     */
    public static File inputStreamToFile(InputStream is, String dirPath, String fileName) {
        try {
            if (is != null) {
                File file = new File(createDirByPath(dirPath), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 100];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.flush();
                fos.close();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public static File createDirByPath(String path) {
        File file = new File(path);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createDirFile(String path) {
        int pos = path.lastIndexOf("/");
        String dirpath = path.substring(0, pos + 1);
        if (!dirpath.startsWith("/")) {
            dirpath = "/" + dirpath;
        }
        File f = new File(dirpath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return new File(path);
    }

    /**
     * 通过文件名，判断指定目录下是否有改文件
     *
     * @param filename
     */
    public static File isExitByFileName(String path, String filename) {
        File isExitFile = null;
        File dir = new File(path);
        File[] listFiles = dir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().equals(filename)) {
                    isExitFile = file;
                    break;
                }
            }
        }
        return isExitFile;
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.getAbsoluteFile().delete();
        }
    }

    /***
     * 删除指定目录下得所有文件
     * @param dirPath
     */
    public static boolean clearAllFile(String dirPath) {
        File file = new File(dirPath);
        if (file != null && file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                for (int i = 0; listFiles != null && i < listFiles.length; i++) {
                    if (listFiles[i].isFile()) {
                        if (!listFiles[i].delete()) {
                            LogUtil.d("Delete file failed!" + dirPath);
                            return false;
                        }
                    } else {
                        if (!clearAllFile(listFiles[i].getPath())) {
                            return false;
                        }
                    }
                }
            }
        }
        LogUtil.d("Delete file----file is not exists----" + dirPath);
        return true;
    }

    /**
     * view 生成图片保存相册
     * @param context
     * @param view
     */
    public static void saveViewToImage(Context context, View view) {
        saveViewToImage(context, view, "");
    }

    /**
     * view 生成图片保存相册
     * @param context
     * @param view
     * @param filename
     */
    public static void saveViewToImage(Context context, View view, String filename) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        //系统相册目录
        String peicurePath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        //确定目录是否存在
        createDirByPath(peicurePath);
        String fileName = TextUtils.isEmpty(filename) ? ("jht_" + System.currentTimeMillis() + ".jpg") : filename;
        File file = new File(peicurePath, fileName);
        LogUtil.d(peicurePath + fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();

                ToastUtil.showCenterToast("名片已保存到相册");
                //通知相册更新
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), longImage, fileName, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort("保存失败");
        }

    }






    /**
     * webview 生成图片保存相册
     *
     * @param mWebView
     */
    public static void saveWebviewToImage(Context context, WebView mWebView) {
        saveWebviewToImage(context, mWebView, "");
    }

    public static void saveWebviewToImage(Context context, WebView mWebView, String filename) {
        /*
        方法一
        Picture picture = mWebView.capturePicture();
        Bitmap longImage = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(longImage);
        picture.draw(c);
        */

        // WebView 生成长图，也就是超过一屏的图片，代码中的 longImage 就是最后生成的长图
        mWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        mWebView.layout(0, 0, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
        mWebView.setDrawingCacheEnabled(true);
        mWebView.buildDrawingCache();

        Bitmap longImage = Bitmap.createBitmap(mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(longImage);  // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        canvas.drawBitmap(longImage, 0, mWebView.getMeasuredHeight(), paint);
        mWebView.draw(canvas);

        //系统相册目录
        String peicurePath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        //确定目录是否存在
        createDirByPath(peicurePath);
        String fileName = TextUtils.isEmpty(filename) ? ("jht_" + System.currentTimeMillis() + ".jpg") : filename;
        File file = new File(peicurePath, fileName);
        LogUtil.d(peicurePath + fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            if (fos != null) {
                longImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                ToastUtil.showShort("保存成功");
                //通知相册更新
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), longImage, fileName, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();

            ToastUtil.showShort("保存失败");
        }
    }


}


