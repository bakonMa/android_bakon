package com.jht.doctor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.jht.doctor.R;
import com.jht.doctor.utils.glide_transformation.RoundedCornersTransformation;
import com.jht.doctor.utils.imageloader.GlideApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;

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
        if (imageView == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .centerCrop()
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
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
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
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .centerCrop()
                .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }
}


