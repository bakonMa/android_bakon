package com.junhetang.doctor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhetang.doctor.R;


/**
 * Created by Tang on 2017/11/13.
 */

public class RelativeWithText extends RelativeLayout {
    private View lineView;

    private TextView tv_title;

    private boolean isShowLine;

    private boolean isShowTab;

    private boolean isShowArrow;

    private String title;

    private TextView tv_tab;

    private ImageView iv_arrow;


    public RelativeWithText(Context context) {
        this(context, null);
    }

    public RelativeWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RelativeWithText);
        isShowLine = ta.getBoolean(R.styleable.RelativeWithText_isShowLineT, true);
        title = ta.getString(R.styleable.RelativeWithText_titleT);
        isShowTab = ta.getBoolean(R.styleable.RelativeWithText_isShowTab, false);
        isShowArrow = ta.getBoolean(R.styleable.RelativeWithText_isShowArrow, true);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_relative_text, this);
        tv_title = findViewById(R.id.id_title);
        lineView = findViewById(R.id.id_line);
        tv_tab = findViewById(R.id.id_tv_tab);
        iv_arrow = findViewById(R.id.id_iv_arrow);
        tv_title.setText(title);
        if (isShowLine) {
            lineView.setVisibility(VISIBLE);
        } else {
            lineView.setVisibility(GONE);
        }
        if (isShowArrow) {
            iv_arrow.setVisibility(VISIBLE);
        } else {
            iv_arrow.setVisibility(GONE);
        }
        if (isShowTab) {
            tv_tab.setVisibility(VISIBLE);
        } else {
            tv_tab.setVisibility(GONE);
        }
    }

    //设置title
    public void setTitleText(String title) {
        tv_title.setText(title);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            tv_tab.setEnabled(true);
            tv_tab.setText("已认证");
            tv_tab.setTextColor(getContext().getResources().getColor(R.color.tab_selected));
        } else {
            tv_tab.setEnabled(false);
            tv_tab.setText("未认证");
            tv_tab.setTextColor(getContext().getResources().getColor(R.color.tab_unselected));
        }
    }
}
