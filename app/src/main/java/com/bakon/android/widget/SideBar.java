package com.bakon.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.utils.UIUtils;

import java.util.Arrays;

public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    private String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "MSG", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int choose = -1;
    private Paint paint = new Paint();

    //    private PopupWindow mPopup;
    private TextView textView;

    public void setB(String[] b) {
        Arrays.sort(b, (s, s2) -> {
            if ("#".equals(s)) return 1;
            else if ("#".equals(s2)) return -1;
            return s.compareTo(s2);
        });
        this.b = b;
        postInvalidate();
    }

    public void setTextView(TextView mTextDialog) {
        this.textView = mTextDialog;
    }


    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * * 重写这个方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (b.length == 0) return;
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / b.length;// 获取每一个字母的高度

        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.color_000));
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(UIUtils.dp2px(MyApplication.getInstance(), 12));
//            AutoUtils.getPercentWidthSize(12);
            // 选中的状态
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.color_main));//黄色
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
//                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (textView != null) {
                    textView.setVisibility(GONE);
                }
                break;

            default:
//                setBackgroundResource(R.drawable.sidebar_background);
//                if (oldChoose != c) {
                if (c >= 0 && c < b.length) {
                    if (listener != null) {
                        listener.onTouchingLetterChanged(b[c], (int) event.getY());
                    }
                    if (textView != null) {
                        textView.setText(b[c]);
                        textView.setVisibility(VISIBLE);
                    }

                    choose = c;
                    invalidate();
                }
//                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s, int offsetY);
    }

}