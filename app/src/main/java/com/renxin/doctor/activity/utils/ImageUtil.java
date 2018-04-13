package com.renxin.doctor.activity.utils;

import android.widget.ImageView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.glide_transformation.RoundedCornersTransformation;
import com.renxin.doctor.activity.utils.imageloader.GlideApp;

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
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .centerCrop()
                .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }
}


