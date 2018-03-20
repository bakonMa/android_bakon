package com.jht.doctor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;


/**
 * Created by Tang on 2017/11/2.
 */

public class EditTextlayout extends RelativeLayout implements View.OnClickListener {
    private Context mContext;

    private TextView leftTextView; //左边的文字

    private EditText mEditText; //主要的输入框

    private ImageView deleteImage;  //清除按钮

    private CheckBox mCheckbox; //睁眼闭眼

    private TextView showText;  //不可编辑的文字

    private boolean isShowCheckBox;

    private boolean isBankCard;

    private float mTextMainSize;

    private String leftTextStr;

    private String editTextHint;

    private int editTextType;

    public EditTextlayout(Context context) {
        this(context, null);
    }

    public EditTextlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextlayout, defStyleAttr, 0);
        mTextMainSize = ta.getDimension(R.styleable.EditTextlayout_textMainSize, 14);
        isShowCheckBox = ta.getBoolean(R.styleable.EditTextlayout_isShowCheckBox, false);
        leftTextStr = ta.getString(R.styleable.EditTextlayout_leftText);
        editTextHint = ta.getString(R.styleable.EditTextlayout_editTextHint);
        isBankCard = ta.getBoolean(R.styleable.EditTextlayout_isBankCard, false);
        editTextType = ta.getInt(R.styleable.EditTextlayout_editTextType, 1);
        ta.recycle();
        initView();
        initEvent();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.edittext_layout, this);
        leftTextView = (TextView) findViewById(R.id.id_tv_left);
        mEditText = (EditText) findViewById(R.id.id_layout_edittext);
        deleteImage = (ImageView) findViewById(R.id.id_btn_clear);
        mCheckbox = (CheckBox) findViewById(R.id.id_checkbox);
        showText = (TextView) findViewById(R.id.id_tv_show_text);
    }

    private void initEvent() {
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        leftTextView.setText(leftTextStr);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        mEditText.setHint(editTextHint);
        showText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextMainSize);
        switch (editTextType) {
            case 1:
                break;
            case 2:
                mEditText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
                mEditText.postInvalidate();
                break;
            case 3:
                mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mEditText.postInvalidate();
                break;
        }
        if (isShowCheckBox) {
            mCheckbox.setVisibility(VISIBLE);

            mCheckbox.setOnClickListener(this);
        }
        if (isBankCard) {
            //todo 如果是银行卡

        }
        //设置焦点变化的监听
        mEditText.setOnFocusChangeListener(new FocusChangeListenerImpl());
        //设置EditText文字变化的监听
        mEditText.addTextChangedListener(new TextWatcherImpl());
        deleteImage.setOnClickListener(this);
    }

    private void setClearImageVisible(boolean isShow) {
        if (isShow) {
            deleteImage.setVisibility(VISIBLE);
        } else {
            deleteImage.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_clear:
                mEditText.setText("");
                break;
            case R.id.id_checkbox:
                changePswVisibility(mCheckbox, mEditText);
        }
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                boolean isVisible = mEditText.getText().toString().length() >= 1;
                setClearImageVisible(isVisible);
            } else {
                setClearImageVisible(false);
            }
        }

    }

    //当输入结束后判断是否显示右边clean的图标
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            boolean isVisible = mEditText.getText().toString().length() >= 1;
            setClearImageVisible(isVisible);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setText(String text) {
        mEditText.setVisibility(GONE);
        showText.setText(text);
        showText.setVisibility(VISIBLE);
    }

    public void setMaxLength(int length) {
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }


    public void changePswVisibility(CheckBox checkBox, EditText editText) {
        if (!checkBox.isChecked()) {
            editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        editText.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = editText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
