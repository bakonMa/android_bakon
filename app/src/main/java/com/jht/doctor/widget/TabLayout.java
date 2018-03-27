package com.jht.doctor.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.utils.UIUtils;

/**
 * Created by Tang on 2017/11/14.
 */

public class TabLayout extends RelativeLayout implements View.OnClickListener {
    private TextView tv1, tv2, tv3;
    private View indicator;
    private float transX1, transX2, transX3;
    private float mCurrentTransX;
    private int mIndex;
    private float tv_width;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_tab, this);
        tv1 = findViewById(R.id.id_tv1);
        tv2 = findViewById(R.id.id_tv2);
        tv3 = findViewById(R.id.id_tv3);
        indicator = findViewById(R.id.id_indicator);
        //设置indicator和文字一样宽
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv1.measure(spec, spec);
        int measuredWidthTicketNum = tv1.getMeasuredWidth();
        LinearLayout.LayoutParams lp_indicator = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        lp_indicator.width = measuredWidthTicketNum;
        indicator.setLayoutParams(lp_indicator);

        //tv1.setOnClickListener(this);
        //tv2.setOnClickListener(this);
        //.setOnClickListener(this);
        mIndex = 0;
        tv_width = UIUtils.getScreenWidth(getContext()) / 3;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LinearLayout.LayoutParams lp_indicator = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        lp_indicator.leftMargin = getLocationX(tv1);
        indicator.setLayoutParams(lp_indicator);
        transX1 = 0;
        transX2 = getLocationX(tv2) - getLocationX(tv1);
        transX3 = getLocationX(tv3) - getLocationX(tv1);
    }

    private int getLocationX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv1:

                break;
            case R.id.id_tv2:

                break;
            case R.id.id_tv3:

        }
    }

    float x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (x >= 0 && x <= tv_width) {
                    if (mIndex != 0) {
                        startAnimation(transX1);
                        mIndex = 0;
                        mCurrentTransX = transX1;
                    }
                    if (mListener != null) {
                        mListener.itemClicked(0);
                    }
                } else if (x > tv_width && x <= tv_width * 2) {
                    if (mIndex != 1) {
                        startAnimation(transX2);
                        mCurrentTransX = transX2;
                        mIndex = 1;
                    }
                    if (mListener != null) {
                        mListener.itemClicked(1);
                    }
                } else {
                    if (mIndex != 2) {
                        startAnimation(transX3);
                        mCurrentTransX = transX3;
                        mIndex = 2;
                    }
                    if (mListener != null) {
                        mListener.itemClicked(2);
                    }
                }
                break;

        }
        return true;
    }

    private void startAnimation(float translationX) {
        mCurrentTransX = indicator.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(indicator, "translationX", mCurrentTransX, translationX);
        animator.setDuration(300);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch (mIndex) {
                    case 0:
                        tv1.setTextColor(getContext().getResources().getColor(R.color.color_main));
                        tv2.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        tv3.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        break;
                    case 1:
                        tv1.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        tv2.setTextColor(getContext().getResources().getColor(R.color.color_main));
                        tv3.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        break;
                    case 2:
                        tv1.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        tv2.setTextColor(getContext().getResources().getColor(R.color.toolbar_title));
                        tv3.setTextColor(getContext().getResources().getColor(R.color.color_main));
                        break;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private ItemClickedListener mListener;

    public void setOnItemClicked(ItemClickedListener listener) {
        this.mListener = listener;
    }

    public interface ItemClickedListener {
        void itemClicked(int position);
    }


}
