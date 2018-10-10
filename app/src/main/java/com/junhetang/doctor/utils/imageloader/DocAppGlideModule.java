package com.junhetang.doctor.utils.imageloader;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author: ZhaoYun
 * @date: 2017/11/6
 * @project: customer-android-2th
 * @detail:
 */
@GlideModule
public final class DocAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }


}
