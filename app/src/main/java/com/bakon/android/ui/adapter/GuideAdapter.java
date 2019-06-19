package com.bakon.android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bakon.android.utils.ImageUtil;

import java.util.List;

/**
 * 全屏透明 引导图
 */
public class GuideAdapter extends PagerAdapter {
    private Context context;
    private List<Integer> imgList;

    private SparseArray<ImageView> mViewList = new SparseArray<>();

    public GuideAdapter(Context context, List<Integer> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView view = mViewList.get(position);
        if (view == null) {
            view = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setImageBitmap(ImageUtil.readBitMap(context, imgList.get(position)));
            view.setLayoutParams(params);
            mViewList.append(position, view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageView imageView = mViewList.get(position);
        //优化 防止oom
        if (imageView != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            if (bitmapDrawable != null) {
                Bitmap bm = bitmapDrawable.getBitmap();
                if (bm != null && !bm.isRecycled()) {
                    imageView.setImageResource(0);
                    bm.recycle();
                }
            }
        }
        container.removeView((View) object);
    }
}