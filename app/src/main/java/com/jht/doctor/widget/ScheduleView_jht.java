package com.jht.doctor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.utils.UIUtils;
import com.jht.doctor.utils.LogUtil;

/**
 * 阶段进度view
 */

public class ScheduleView_jht extends View {
    private int defalutRadius;

    private int lineWidth;
    //文字画笔
    private Paint textPaint;
    //线点画笔
    private Paint linePointPaint;
    //线画笔
    private Paint linePaint;
    //阶段圆圈 画笔
    private Paint roundPaint;

    private String[] texts = new String[]{"填写资料", "上传证件", "系统审核", "认证成功"};

    private int lineHeight = 2;

    private int mWidth;

    private int mHeight;

    private Context mContext;

    private int marginY;

    private int textSize = 12;

    private Rect textBound;

    private int baseX, baseY, baseTextY;
    //3个小球的把半径
    private int pointRadiusLittle, pointRadiusMiddle, pointRadiusBig;

    private int pointSpace;
    private int currentIndex;

    private Bitmap bitmap_current, bitmap_compeled;


    public ScheduleView_jht(Context context) {
        this(context, null);
    }

    public ScheduleView_jht(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleView_jht(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScheduleView_jht);
        currentIndex = ta.getInt(R.styleable.ScheduleView_jht_step1, 1);
        LogUtil.d("currentIndex  ====" + currentIndex);
        ta.recycle();
        init();
    }

    private void init() {
        LogUtil.d("init currentIndex  ====" + currentIndex);
        //文字画笔
        textPaint = new Paint();
        textPaint.setTextSize(UIUtils.sp2px(mContext, textSize));
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);
        //文字选中颜色
        textPaint.setColor(getContext().getResources().getColor(R.color.white));
        //线原点画笔
        linePointPaint = new Paint();
        linePointPaint.setAntiAlias(true);
        linePointPaint.setStyle(Paint.Style.FILL);
        linePointPaint.setColor(getContext().getResources().getColor(R.color.white));
        //线画笔
        linePaint = new Paint();
        linePaint.setStrokeWidth(UIUtils.dp2px(getContext(), lineHeight));
        linePaint.setColor(getContext().getResources().getColor(R.color.white));

        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setColor(getContext().getResources().getColor(R.color.white));

        defalutRadius = UIUtils.dp2px(mContext, 13);
        lineWidth = UIUtils.dp2px(mContext, 64);
        marginY = UIUtils.dp2px(mContext, 9);

        pointSpace = UIUtils.dp2px(mContext, 10);
        pointRadiusLittle = UIUtils.dp2px(mContext, 3);
        pointRadiusMiddle = UIUtils.dp2px(mContext, 4);
        pointRadiusBig = UIUtils.dp2px(mContext, 5);

        bitmap_current = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_jxz);
        bitmap_compeled = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_ywc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //圆和边的距离
        baseX = (getTextWith(texts[0]) - defalutRadius * 2) / 2;
        baseY = defalutRadius;

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();//获取文字的高度
        mWidth = defalutRadius * 8 + lineWidth * 3 + (getTextWith(texts[0]) - defalutRadius * 2);
        mHeight = defalutRadius * 2 + marginY + (int) (fontMetrics.bottom - fontMetrics.top);
//        Log.e("measure", mWidth + "-" + mHeight);
        baseTextY = (int) (mHeight - fontMetrics.leading - fontMetrics.descent);

        setMeasuredDimension(mWidth, mHeight);
    }

    //获取一个字符串的宽度
    private int getTextWith(String text) {
        textBound = new Rect();
        textPaint.getTextBounds(texts[0], 0, text.length(), textBound);
        return textBound.width();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        LogUtil.d("onDraw  start");
        super.onDraw(canvas);
        //onDraw重绘 初始化一下状态
        roundPaint.setColor(mContext.getResources().getColor(R.color.white));
        textPaint.setColor(mContext.getResources().getColor(R.color.white));

        drawTextPointLine(canvas);
        drawBitmap(canvas);
    }

    private void drawPointLine(Canvas canvas, int start, boolean isWhite) {
        linePointPaint.setColor(mContext.getResources().getColor(isWhite ? R.color.white : R.color.color_999));
        canvas.drawCircle(start + pointSpace + pointRadiusLittle, baseY, pointRadiusLittle, linePointPaint);
        canvas.drawCircle(start + pointSpace * 2 + pointRadiusMiddle * 2, baseY, pointRadiusMiddle, linePointPaint);
        canvas.drawCircle(start + pointSpace * 3 + pointRadiusMiddle * 2 + pointRadiusBig * 2, baseY, pointRadiusBig, linePointPaint);
    }

    private void drawTextPointLine(Canvas canvas) {
        switch (currentIndex) {
            case 1:
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                textPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawText(texts[1], baseX + defalutRadius * 3 + lineWidth - getTextWith(texts[1]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[2], baseX + defalutRadius * 5 + lineWidth * 2 - getTextWith(texts[2]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[3], baseX + defalutRadius * 7 + lineWidth * 3 - getTextWith(texts[3]) / 2, baseTextY, textPaint);

                //圆和线
                canvas.drawCircle(baseX + defalutRadius, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 2, true);
                roundPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawCircle(baseX + defalutRadius * 3 + lineWidth, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 4 + lineWidth, false);
                canvas.drawCircle(baseX + defalutRadius * 5 + lineWidth * 2, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 6 + lineWidth * 2, false);
                canvas.drawCircle(baseX + defalutRadius * 7 + lineWidth * 3, baseY, defalutRadius, roundPaint);
                break;
            case 2:
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                canvas.drawText(texts[1], baseX + defalutRadius * 3 + lineWidth - getTextWith(texts[1]) / 2, baseTextY, textPaint);
                textPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawText(texts[2], baseX + defalutRadius * 5 + lineWidth * 2 - getTextWith(texts[2]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[3], baseX + defalutRadius * 7 + lineWidth * 3 - getTextWith(texts[3]) / 2, baseTextY, textPaint);

                //圆和线
                canvas.drawCircle(baseX + defalutRadius, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 2, true);
                canvas.drawCircle(baseX + defalutRadius * 3 + lineWidth, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 4 + lineWidth, true);
                roundPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawCircle(baseX + defalutRadius * 5 + lineWidth * 2, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 6 + lineWidth * 2, false);
                canvas.drawCircle(baseX + defalutRadius * 7 + lineWidth * 3, baseY, defalutRadius, roundPaint);
                break;
            case 3:
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                canvas.drawText(texts[1], baseX + defalutRadius * 3 + lineWidth - getTextWith(texts[1]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[2], baseX + defalutRadius * 5 + lineWidth * 2 - getTextWith(texts[2]) / 2, baseTextY, textPaint);
                textPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawText(texts[3], baseX + defalutRadius * 7 + lineWidth * 3 - getTextWith(texts[3]) / 2, baseTextY, textPaint);

                //圆和线
                canvas.drawCircle(baseX + defalutRadius, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 2, true);
                canvas.drawCircle(baseX + defalutRadius * 3 + lineWidth, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 4 + lineWidth, true);
                canvas.drawCircle(baseX + defalutRadius * 5 + lineWidth * 2, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 6 + lineWidth * 2, true);
                roundPaint.setColor(mContext.getResources().getColor(R.color.color_999));
                canvas.drawCircle(baseX + defalutRadius * 7 + lineWidth * 3, baseY, defalutRadius, roundPaint);
                break;
            case 4:
                canvas.drawText(texts[0], 0, baseTextY, textPaint);
                canvas.drawText(texts[1], baseX + defalutRadius * 3 + lineWidth - getTextWith(texts[1]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[2], baseX + defalutRadius * 5 + lineWidth * 2 - getTextWith(texts[2]) / 2, baseTextY, textPaint);
                canvas.drawText(texts[3], baseX + defalutRadius * 7 + lineWidth * 3 - getTextWith(texts[3]) / 2, baseTextY, textPaint);

                //圆和线
                canvas.drawCircle(baseX + defalutRadius, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 2, true);
                canvas.drawCircle(baseX + defalutRadius * 3 + lineWidth, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 4 + lineWidth, true);
                canvas.drawCircle(baseX + defalutRadius * 5 + lineWidth * 2, baseY, defalutRadius, roundPaint);
                drawPointLine(canvas, baseX + defalutRadius * 6 + lineWidth * 2, true);
                canvas.drawCircle(baseX + defalutRadius * 7 + lineWidth * 3, baseY, defalutRadius, roundPaint);
                break;
        }
    }

    //图片
    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(bitmap_compeled, null
                , new Rect(baseX, 0, baseX + defalutRadius * 2, defalutRadius * 2)
                , null);
        canvas.drawBitmap(bitmap_compeled, null
                , new Rect(baseX + defalutRadius * 2 + lineWidth, 0, baseX + defalutRadius * 4 + lineWidth, defalutRadius * 2)
                , null);
        canvas.drawBitmap(bitmap_compeled, null
                , new Rect(baseX + defalutRadius * 4 + lineWidth * 2, 0, baseX + defalutRadius * 6 + lineWidth * 2, defalutRadius * 2)
                , null);
        canvas.drawBitmap(bitmap_compeled, null
                , new Rect(baseX + defalutRadius * 6 + lineWidth * 3, 0, baseX + defalutRadius * 8 + lineWidth * 3, defalutRadius * 2)
                , null);
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        invalidate();
    }
}
