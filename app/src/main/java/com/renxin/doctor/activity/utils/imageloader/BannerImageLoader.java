package com.renxin.doctor.activity.utils.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.renxin.doctor.activity.utils.ImageUtil;
import com.youth.banner.loader.ImageLoader;

/**
 * BannerImageLoader
 * Create at 2018/4/16 上午10:22 by mayakun 
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageUtil.showImage(path.toString(),imageView);
    }
}
