package com.renxin.doctor.activity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.UIUtils;

/**
 * Created by Tang on 2017/11/8.
 */

public class ScheduleView extends View {
    private int defalutRadius;

    private int selectedRadius;

    private int lineWidth;

    private Paint textPaint;

    private Paint linePaint;

    private Paint roundPaint;

    private String[] texts = new String[]{"基本信息", "工作信息", "房产信息"};

    private int lineHeight = 2;

    private int mWidth;

    private int mHeight;

    private Context mContext;

    private int marginY;

    private int textSize = 14;

    private Rect textBound;

    private int twoTextWidth;

    private int baseX, baseY, baseTextY;

    private int currentIndex;

    private Bitmap bitmap_current, bitmap_compeled;


    public ScheduleView(Context context) {
        this(context, null);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScheduleView);
        currentIndex = ta.getInt(R.styleable.ScheduleView_step,1);
        ta.recycle();
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(UIUtils.sp2px(mContext, textSize));
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(UIUtils.dp2px(getContext(), lineHeight));
        linePaint.setColor(getContext().getResources().getColor(R.color.color_f0f0f0));

        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setColor(getContext().getResources().getColor(R.color.color_e8e8e8));

        defalutRadius = UIUtils.dp2px(mContext, 9);
        selectedRadius = UIUtils.dp2px(mContext, 13);
        lineWidth = UIUtils.dp2px(mContext, 110);
        marginY = UIUtils.dp2px(mContext, 9);

        bitmap_current = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_jxz);
        bitmap_compeled = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_ywc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        textBound = new Rect();
        textPaint.getTextBounds(texts[1], 0, 2, textBound);
        twoTextWidth = textBound.width();
        baseX = textBound.width();
        baseY = selectedRadius;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        mWidth = defalutRadius * 4 + lineWidth * 2 + textBound.width() * 2;
        mHeight = selectedRadius * 2 + marginY + (int) (fontMetrics.bottom - fontMetrics.top);
        Log.e("measure", mWidth + "-" + mHeight);
        baseTextY = (int) (mHeight - fontMetrics.leading - fontMetrics.descent);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRoundLine(canvas);
        drawText(canvas);
        drawBitmap(canvas);
    }

    private void drawRoundLine(Canvas canvas) {
        canvas.drawCircle(baseX, baseY, defalutRadius, roundPaint);
        canvas.drawLine(baseX + defalutRadius, baseY, baseX + defalutRadius + lineWidth, baseY, linePaint);
        canvas.drawCircle(baseX + defalutRadius * 2 + lineWidth, baseY, defalutRadius, roundPaint);
        canvas.drawLine(baseX + defalutRadius * 3 + lineWidth, baseY, baseX + defalutRadius * 3 + lineWidth * 2, baseY, linePaint);
        canvas.drawCircle(baseX + defalutRadius * 4 + lineWidth * 2, baseY, defalutRadius, roundPaint);
    }

    private void drawText(Canvas canvas) {
        switch (currentIndex) {
            case 1:
                textPaint.setColor(mContext.getResources().getColor(R.color.color_333));
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                textPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawText(texts[1], mWidth / 2 - twoTextWidth, baseTextY, textPaint);
                canvas.drawText(texts[2], mWidth - twoTextWidth * 2 - 5, baseTextY, textPaint);
                break;
            case 2:
                textPaint.setColor(mContext.getResources().getColor(R.color.color_333));
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                canvas.drawText(texts[1], mWidth / 2 - twoTextWidth, baseTextY, textPaint);
                textPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawText(texts[2], mWidth - twoTextWidth * 2 - 5, baseTextY, textPaint);
                break;
            case 3:
            case 4:
                textPaint.setColor(mContext.getResources().getColor(R.color.color_333));
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                canvas.drawText(texts[1], mWidth / 2 - twoTextWidth, baseTextY, textPaint);
                canvas.drawText(texts[2], mWidth - twoTextWidth * 2 - 5, baseTextY, textPaint);
                break;
        }
    }

    private void drawBitmap(Canvas canvas) {
        switch (currentIndex) {
            case 1:
                canvas.drawBitmap(bitmap_current, null
                        , new Rect(baseX - selectedRadius, 0, baseX + selectedRadius, baseY + selectedRadius)
                        , null);
                break;
            case 2:
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX - selectedRadius, 0, baseX + selectedRadius, baseY + selectedRadius)
                        , null);
                canvas.drawBitmap(bitmap_current, null
                        , new Rect(baseX + defalutRadius * 2 + lineWidth - selectedRadius, 0, baseX + defalutRadius * 2 + lineWidth + selectedRadius, baseY + selectedRadius)
                        , null);
                break;
            case 3:
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX - selectedRadius, 0, baseX + selectedRadius, baseY + selectedRadius)
                        , null);
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX + defalutRadius * 2 + lineWidth - selectedRadius, 0, baseX + defalutRadius * 2 + lineWidth + selectedRadius, baseY + selectedRadius)
                        , null);
                canvas.drawBitmap(bitmap_current, null
                        , new Rect(baseX + defalutRadius * 4 + lineWidth * 2 - selectedRadius, 0, baseX + defalutRadius * 4 + lineWidth * 2 + selectedRadius, baseY + selectedRadius)
                        , null);
                break;
            case 4:
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX - selectedRadius, 0, baseX + selectedRadius, baseY + selectedRadius)
                        , null);
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX + defalutRadius * 2 + lineWidth - selectedRadius, 0, baseX + defalutRadius * 2 + lineWidth + selectedRadius, baseY + selectedRadius)
                        , null);
                canvas.drawBitmap(bitmap_compeled, null
                        , new Rect(baseX + defalutRadius * 4 + lineWidth * 2 - selectedRadius, 0, baseX + defalutRadius * 4 + lineWidth * 2 + selectedRadius, baseY + selectedRadius)
                        , null);
                break;
        }
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        invalidate();
    }
}
