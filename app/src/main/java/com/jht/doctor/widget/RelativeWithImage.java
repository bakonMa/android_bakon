package com.jht.doctor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;


/**
 * Created by Tang on 2017/11/13.
 */

public class RelativeWithImage extends RelativeLayout {
    private ImageView iv_left;

    private View lineView;

    private TextView tv_title;

    private boolean isShowLine;

    private int leftImage;

    private String title;


    public RelativeWithImage(Context context) {
        this(context, null);
    }

    public RelativeWithImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeWithImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RelativeWithImage);
        isShowLine = ta.getBoolean(R.styleable.RelativeWithImage_isShowLine,true);
        leftImage = ta.getResourceId(R.styleable.RelativeWithImage_leftIamge,R.drawable.icon_ziliao);
        title = ta.getString(R.styleable.RelativeWithImage_title);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_relative_img,this);
        iv_left = findViewById(R.id.id_image);
        tv_title = findViewById(R.id.id_title);
        lineView = findViewById(R.id.id_line);
        iv_left.setImageResource(leftImage);
        tv_title.setText(title);
        if(isShowLine){
            lineView.setVisibility(VISIBLE);
        }else{
            lineView.setVisibility(GONE);
        }
    }
}
