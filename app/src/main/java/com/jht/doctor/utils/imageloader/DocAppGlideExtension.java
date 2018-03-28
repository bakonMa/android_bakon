package com.jht.doctor.utils.imageloader;

import com.bumptech.glide.annotation.GlideExtension;

/**
 * @author: ZhaoYun
 * @date: 2017/11/6
 * @project: customer-android-2th
 * @detail:
 */
@GlideExtension
public final class DocAppGlideExtension {

    private DocAppGlideExtension(){
    }

//    @GlideOption
//    public static void myRequestOptionExtension(RequestOptions requestOptions , int size){
//        requestOptions.fitCenter().override(size);
//        //外部调用ImageLoader.with(fragment).load(url).myRequestOptionExtension(mySize).into(imageView);
//    }
//
//    @GlideType(PictureDrawable.class)
//    public static void asPictureDrawable(RequestBuilder<PictureDrawable> pictureDrawableRequestBuilder){
//        pictureDrawableRequestBuilder.transition(new DrawableTransitionOptions()).apply(new RequestOptions());
//        //外部调用ImageLoader.with(fragment).asPictureDrawable().load(url).inito(imageView);
//    }

}
