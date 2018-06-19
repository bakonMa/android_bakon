package com.junhetang.doctor.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.glide_transformation.RoundedCornersTransformation;
import com.junhetang.doctor.utils.imageloader.GlideApp;

import java.io.File;
import java.io.InputStream;

/**
 * ImageUtil
 * Create by mayakun at 2018/3/28 上午11:27
 */
public class ImageUtil {
//    CenterCrop 缩放宽和高都到达View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能超过边界
//    CenterInside 如果宽和高都在View的边界内，那就不缩放，否则缩放宽和高都进入View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能在边界内
//    CircleCrop 圆形且结合了CenterCrop的特性
//    FitCenter 缩放宽和高都进入View的边界，有一个参数在边界上，另一个参数可能在边界上，也可能在边界内

    /**
     * 显示图片
     *
     * @param url
     * @param imageView
     */
    public static void showImage(String url, ImageView imageView) {
        showImage(url, imageView, R.drawable.default_img);
    }

    /**
     * 显示图片
     *
     * @param url
     * @param imageView
     */
    public static void showImage(String url, ImageView imageView, int default_img) {
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(default_img == 0 ? R.drawable.default_img : default_img)
                .error(default_img == 0 ? R.drawable.default_img : default_img)
                .into(imageView);
    }

    /**
     * 显示圆形头像
     *
     * @param url
     * @param imageView
     */
    public static void showCircleImage(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .circleCrop() //圆形
                .into(imageView);
    }

    /**
     * 显示圆角图片
     *
     * @param url
     * @param corner
     * @param imageView
     */
    public static void showRoundImage(String url, int corner, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.default_head)
                .error(R.drawable.default_head)
                .centerCrop()
                .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }


    /**
     * 相片剪切 添加7.0版本支持
     *
     * @param uri
     * @param mContext
     */
    public static Uri cropUri;

    public static void doCrop(Activity activity, File file, int requestCode) {
        //防止 第一次操作后 一直显示同一张的问题
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/JHTIMG/";
        LogUtil.d("doCrop dirPath=" + dirPath);
        String name = System.currentTimeMillis() + ".jpg";
        FileUtil.clearAllFile(dirPath);
        FileUtil.createDirByPath(dirPath);

        //裁剪后的图片所有版本都用Uri.fromFile
//        cropUri = Uri.fromFile(new File(path));
        cropUri = Uri.parse("file://" + "/" + dirPath + name);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(UriUtil.getUri(activity, file), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("circleCrop", "false");//圆形
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", 300);//输出X方向的像素 //有些手机（6.0，尺寸太大不行）
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);//设置为不返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);//裁剪后的图片路径

        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 本地图片转换bitmap
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}


