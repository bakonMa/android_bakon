package com.junhetang.doctor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

/**
 * MaxHeightScrollView 高度为屏幕的2/3
 * Create at 2018/8/23 下午6:04 by mayakun
 */
public class MaxHeightScrollView extends ScrollView {

    public MaxHeightScrollView(Context context) {
        super(context);

    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Context context = getContext();
        if (null != context) {
            int screenHeight = getScreenHeight(context);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(screenHeight * 2 / 3, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }


}