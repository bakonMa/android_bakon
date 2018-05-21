package com.junhetang.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.UIUtils;


/**
 * Created by mayakun on 2017/12/6.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private int layout;

    private boolean isSingelBtn;//true：只有确定 false：确定和取消(显示模式)
    private EditText commEditext;
    private boolean isInput = false;//是否显示输入框
    private String titleStr;
    private View layoutView;
    private View.OnClickListener listener;

    public CommonDialog(@NonNull Activity context, int layout, View.OnClickListener listener) {
        this(context, layout, "", listener);
    }

    public CommonDialog(@NonNull Activity context, int layout, String title, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.layout = layout;
        this.titleStr = title;
        this.listener = listener;
    }

    public CommonDialog(@NonNull Activity context, boolean isSingelBtn, String titleStr, View.OnClickListener listener) {
        this(context, isSingelBtn, false, titleStr, listener);
    }

    public CommonDialog(@NonNull Activity context, String titleStr) {
        this(context, true, titleStr, null);
    }

    //是否显示输入框
    public CommonDialog(@NonNull Activity context, boolean isSingelBtn, boolean isInput, String titleStr, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.isSingelBtn = isSingelBtn;
        this.layout = R.layout.dialog_common;
        this.titleStr = titleStr;
        this.isInput = isInput;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        switch (layout) {
            case R.layout.dialog_common://普通 确定，取消；是否显示数图库
                layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
                commEditext = layoutView.findViewById(R.id.dialog_content);
                Button buttonNo = layoutView.findViewById(R.id.btn_no);
                Button buttonOK = layoutView.findViewById(R.id.btn_ok);
                buttonNo.setOnClickListener(this);
                buttonOK.setOnClickListener(this);
                //是否只显示确认
                if (isSingelBtn) {
                    buttonNo.setVisibility(View.GONE);
                }
                commEditext.setVisibility(isInput ? View.VISIBLE : View.GONE);
                //title
                if (!TextUtils.isEmpty(titleStr)) {
                    ((TextView) layoutView.findViewById(R.id.dialog_title)).setText(titleStr);
                }
                break;
            case R.layout.dialog_auth:// 提示认证
                layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_auth, null);
                //title
                if (!TextUtils.isEmpty(titleStr)) {
                    ((TextView) layoutView.findViewById(R.id.dialog_title)).setText(titleStr);
                }
                TextView noBtn = layoutView.findViewById(R.id.btn_no);
                noBtn.setOnClickListener(this);
                TextView okBtn = layoutView.findViewById(R.id.btn_gotuauth);
                okBtn.setOnClickListener(this);
                //未认证 显示"去认证"， 认证中 不显示
                if (UIUtils.getString(R.string.str_auth_ing).equals(titleStr)) {//认证中。。。。
                    okBtn.setText("确定");
                    noBtn.setVisibility(View.GONE);
                    okBtn.setId(0);//改变id 不需要点击跳转
                }
                //title
                if (!TextUtils.isEmpty(titleStr)) {
                    ((TextView) layoutView.findViewById(R.id.dialog_title)).setText(titleStr);
                }
                break;
        }

        setContentView(layoutView);
        setCancelable(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
        dismiss();
    }

    //dialog_edite_common 使用
    public String getCommonEditText() {
        if (commEditext == null || commEditext.getText().length() == 0) {
            return "";
        } else {
            return commEditext.getText().toString().trim();
        }

    }

}
