package com.jht.doctor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.utils.UIUtils;

/**
 * Created by Tang on 2017/11/7.
 */

public class SplashIndicator extends View {
    private int mWidth;

    private int mHeight;

    private float itemWidth;

    private ViewPager mViewPager;

    private Paint itemPaint;

    private int mIndex = 0;

    private float mOffsetX;

    public SplashIndicator(Context context) {
        this(context, null);
    }

    public SplashIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        itemPaint = new Paint();
        itemPaint.setAntiAlias(true);
        itemPaint.setStyle(Paint.Style.FILL);
        itemPaint.setColor(getContext().getResources().getColor(R.color.indicator_selected_color));
        setBackgroundResource(R.drawable.bg_indicator);
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                trigger(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                trigger(position, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void trigger(int position, float positionOffset) {
        this.mIndex = position;
        this.mOffsetX = positionOffset * itemWidth;
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        itemWidth = mWidth / 4.0F;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(new RectF(mIndex * itemWidth + mOffsetX, 0, (mIndex + 1) * itemWidth + mOffsetX, mHeight)
                , UIUtils.dp2px(getContext(),1.0f)
                , UIUtils.dp2px(getContext(),1.0f)
                , itemPaint);
    }


}
