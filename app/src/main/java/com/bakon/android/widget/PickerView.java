package com.bakon.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bakon.android.R;

import java.util.List;

/**
 * Created by WangLu on 16/8/24.
 */
public class PickerView extends ScrollView {

    private Context context;
    /**
     * 数据源
     */
    private List<String> mData;
    private String[] mDataStr;
    /**
     * 两条线的画笔
     */
    private Paint mPaint;
    /**
     * 宽,高,还有显示的TextView的高度（注意:这里只是TextView的高度,不算margin）
     */
    private int mWidth, mHeight, mTextHeight;
    /**
     * TextView的父容器
     */
    private LinearLayout mTextGroup;
    /**
     * 获取屏幕的密度
     */
    private float mDensity;
    /**
     * 字体的大小
     */
    private float mTextSize;
    /**
     * 根据字体大小来控制整个控件的高度
     */
    private float mWrapContentHeight;
    /**
     * 当前选中的position
     */
    private int position, tempPosition = -1;
    private int tempScrollTo = 0;
    private int scrollY;

    private int visibleCount;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        /**
         * 这句话设置ScrollView滑动到极限的时候不显示提示（就是那个阴影）
         */
        setOverScrollMode(OVER_SCROLL_NEVER);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView, defStyleAttr, 0);
        try {
            /**
             * 获取到用户设置的字体大小,默认25
             */
            mTextSize = a.getDimension(R.styleable.PickerView_textSize, 16);
            /**
             * 获取到屏幕的密度来设置TextView的高度
             */
            Paint textPaint = new Paint();
            textPaint.setTextSize(mTextSize);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            mTextHeight = (int) (fontMetrics.bottom-fontMetrics.top+0.5);
           /* mDensity = getResources().getDisplayMetrics().density;
            mTextHeight = (int) ((mDensity + 0.5) * mTextSize);*/

            visibleCount = a.getInt(R.styleable.PickerView_visibleCount, 5);
            /**
             * 整个控件的高度为6个TextView的高度
             */
            mWrapContentHeight = mTextHeight * visibleCount * 2;


        } finally {
            a.recycle();
        }
        init();
    }


    private void init() {

        /**
         * 初始化数据,首先添加Group
         */
        mTextGroup = new LinearLayout(context);
        mTextGroup.setOrientation(LinearLayout.VERTICAL);
        mTextGroup.setGravity(Gravity.CENTER);
        addView(mTextGroup);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.WHITE);

        /**
         * 设置背景,这里选择画一个,两条线
         */
        setBackground(new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawRect(0, mHeight / 2 - mTextHeight, mWidth, mHeight / 2 + mTextHeight, mPaint);
            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        });
    }

    /**
     * 设置数据源
     */
    public void setData(List<String> data) {
        mData = data;
        /**
         * 由于我们需要给自身的数据在选中的框里显示
         * 所以这里需要添加前面和后面的空数据
         */
        removeAllViews();
        mTextGroup.removeAllViews();
        addView(mTextGroup);
        for (int i = 0; i < visibleCount / 2; i++) {
            mTextGroup.addView(createTextView(""));
        }
        for (int i = 0; i < mData.size(); i++) {
            mTextGroup.addView(createTextView("" + mData.get(i)));
        }
        for (int i = 0; i < visibleCount / 2; i++) {
            mTextGroup.addView(createTextView(""));
        }
        setPosition(0);
        setTextColor();
    }

    public void clearView(){
        removeAllViews();
        mTextGroup.removeAllViews();
        setPosition(0);
    }

    /**
     * 设置数据源(字符串)
     */
    public void setData(String[] data) {
        mDataStr = data;
        /**
         * 由于我们需要给自身的数据在选中的框里显示
         * 所以这里需要添加前面和后面的空数据
         */
        removeAllViews();
        mTextGroup.removeAllViews();
        addView(mTextGroup);
        for (int i = 0; i < visibleCount / 2; i++) {
            mTextGroup.addView(createTextView(""));
        }
        for (int i = 0; i < mDataStr.length; i++) {
            mTextGroup.addView(createTextView("" + mDataStr[i]));
        }
        for (int i = 0; i < visibleCount / 2; i++) {
            mTextGroup.addView(createTextView(""));
        }
        setPosition(0);
        setTextColor();
    }

    /**
     * 动态创建TextView
     */
    private TextView createTextView(String s) {
        TextView tv = new TextView(context);
        tv.setText(s);
        tv.setTextColor(getResources().getColor(R.color.toolbar_title));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
        tv.setPadding(0, mTextHeight / 2, 0, mTextHeight / 2);
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTextHeight * 2);
        tv.setLayoutParams(params);
        return tv;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 更改测量方法
         * 指定高度为mWrapContentHeight
         */
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = (int) mWrapContentHeight;
        setMeasuredDimension(mWidth, (int) mWrapContentHeight);
    }


    private int measureWidth(int size) {
        int mode = MeasureSpec.getMode(size);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                return (int) mWrapContentHeight;
            case MeasureSpec.EXACTLY:
            default:
                return MeasureSpec.getSize(size);
        }
    }

    /**
     * ScrollView的滑动事件监听
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        /**
         * 计算出当前在两条线里的position
         */
        position = (t + mTextHeight) / (mTextHeight * 2);
        /**
         * 因为此方法会在滑动的时候不停的调用,所以这里设置一个临时的变量来控制
         */
        if (tempPosition != position) {
            setTextColor();
        }
        tempPosition = position;

    }

    public void setTextColor() {
        int size = mTextGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            TextView tv = (TextView) mTextGroup.getChildAt(i);
            /**
             * 因为我们在数据开头添加了一个空的数据,所以这里position要+1
             */
            if (position + visibleCount / 2 == i) {
                tv.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                tv.setTextColor(getResources().getColor(R.color.toolbar_title));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /**
         * 因为ScrollView没有停止滑动的监听,所以这里取巧
         * 在手指离开屏幕的30ms后判断是否和原来的scrollY一样
         * 如果一样则进入,如果不一样则设置为一样
         */
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollY = getScrollY();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (scrollY == getScrollY()) {
                        /**
                         * 获得每次松手后scrollY相对于TextView高度的偏移量
                         */
                        int offset = scrollY % (mTextHeight * 2);
                        /**
                         * 如果偏移量大于TextView高度的一半
                         * 则进入到下一个
                         */
                        int scrollTo;
                        if (offset > mTextHeight) {
                            scrollTo = scrollY - offset + (mTextHeight * 2);
                            smoothScrollTo(0, scrollTo);
                        } else {
                            scrollTo = scrollY - offset;
                            smoothScrollTo(0, scrollTo);
                        }
                        if (mListener != null && scrollTo != tempScrollTo) {
                            mListener.onItemChanged(position);
                            tempScrollTo = scrollTo;
                        }
                    } else {
                        scrollY = getScrollY();
                        post(this);
                    }
                }
            }, 30);
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 设置fling的速度为原来的1/3
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }


    /**
     * 设置position
     */
    public void setPosition(int position) {
        this.position = position;
        if (position == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    scrollTo(0, 1);
                }
            });
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                scrollTo(0, PickerView.this.position * (mTextHeight * 2));
            }
        });
    }

    /**
     * 获取position
     *
     * @return
     */
    public int getPosition() {
        return position;
    }

    private ItemSelectedListerner mListener;

    public void setOnItemChangedListener(ItemSelectedListerner itemSelectedListerner) {
        this.mListener = itemSelectedListerner;
    }

    public interface ItemSelectedListerner {
        void onItemChanged(int postion);
    }

    public String getItem() {
        return ((TextView) mTextGroup.getChildAt(position + 2)).getText().toString();
    }
}
