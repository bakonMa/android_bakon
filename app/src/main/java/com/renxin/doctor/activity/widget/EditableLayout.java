package com.renxin.doctor.activity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renxin.doctor.activity.R;


/**
 * Created by Tang on 2017/11/8.
 */

public class EditableLayout extends RelativeLayout {
    private Context mContext;
    private float mTextMainSize;

    private String leftTextStr;

    private String editTextHint;

    private boolean isShowLine;//是否显示线

    private int editTextType;

    private int mode;

    private TextView tv_left, tv_show, tv_select, tv_yuan;
    private View line;

    private EditText ed_editable;

    private LinearLayout ll_select;

    private ImageView iv_arrow;

    public EditableLayout(Context context) {
        this(context, null);
    }

    public EditableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditableLayout, defStyleAttr, 0);
        mTextMainSize = ta.getDimension(R.styleable.EditableLayout_edit_textMainSize, 14);
        leftTextStr = ta.getString(R.styleable.EditableLayout_edit_leftText);
        editTextHint = ta.getString(R.styleable.EditableLayout_edit_editTextHint);
        editTextType = ta.getInt(R.styleable.EditableLayout_edit_editTextType, 1);
        mode = ta.getInt(R.styleable.EditableLayout_edit_mode, 1);
        isShowLine = ta.getBoolean(R.styleable.EditableLayout_isShowLineBottom, true);
        ta.recycle();
        initView();
        initEvent();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.editable_layout, this);
        tv_left = (TextView) findViewById(R.id.id_tv_left);
        ed_editable = (EditText) findViewById(R.id.id_edittext);
        tv_show = (TextView) findViewById(R.id.id_showtext);
        tv_select = (TextView) findViewById(R.id.id_tv_select);
        ll_select = (LinearLayout) findViewById(R.id.id_ll_select);
        iv_arrow = findViewById(R.id.id_iv_arrow);
        tv_yuan = findViewById(R.id.id_yuan);
        line = findViewById(R.id.id_line);
    }

    private void initEvent() {
        tv_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        tv_left.setText(leftTextStr);
        tv_yuan.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        ed_editable.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        ed_editable.setHint(editTextHint);
        tv_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        tv_select.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        line.setVisibility(isShowLine ? VISIBLE : GONE);
        switch (editTextType) {
            case 1:
                break;
            case 2:
                ed_editable.setInputType(InputType.TYPE_CLASS_NUMBER);
                ed_editable.postInvalidate();
                break;
            case 3:
                ed_editable.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                ed_editable.postInvalidate();
                break;
        }

        switch (mode) {
            case 1://edite模式
                ed_editable.setVisibility(VISIBLE);
                ll_select.setVisibility(GONE);
                break;
            case 2:
                //select模式
                if (!TextUtils.isEmpty(editTextHint)) {
                    tv_select.setHint(editTextHint);
                }
                ed_editable.setVisibility(GONE);
                ll_select.setVisibility(VISIBLE);
                break;
        }
    }

    public EditText getEditText() {
        return ed_editable;
    }

    public TextView getSelectTextView() {
        return tv_select;
    }

    public void setText(String str) {
        switch (mode) {
            case 1:
                //tv_show.setText(str);
                ed_editable.setText(str);
                break;
            case 2:
                tv_select.setText(str);
                break;
        }
    }

    public String getText() {
        switch (mode) {
            case 1:
                return ed_editable.getText().toString().trim();
            case 2:
                return tv_select.getText().toString();
        }
        return "";
    }

    public void setMaxLength(int length) {
        ed_editable.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public void showUnit(String unit) {
        tv_yuan.setText(unit);
    }

    public void hideUnit() {
        tv_yuan.setText("");
    }
}
