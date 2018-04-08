package com.jht.doctor.widget.dialog;

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

import com.jht.doctor.R;


/**
 * Created by mayakun on 2017/12/6.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private int layout;

    private int type;//0：只有确定 1：确定和取消(显示模式)
    private EditText commEditext;

    private String titleStr;
    private View layoutView;
    private View.OnClickListener listener;

    public CommonDialog(@NonNull Activity context, int layout, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.layout = layout;
        this.listener = listener;
    }

    public CommonDialog(@NonNull Activity context, int type, String titleStr, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.type = type;
        this.layout = R.layout.dialog_common;
        this.titleStr = titleStr;
        this.listener = listener;
    }

    public CommonDialog(@NonNull Activity context, String titleStr) {
        this(context, 0, titleStr, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        switch (layout) {
            case R.layout.dialog_common://普通 确定，取消
                layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
                Button buttonNo = layoutView.findViewById(R.id.btn_no);
                Button buttonOK = layoutView.findViewById(R.id.btn_ok);
                buttonNo.setOnClickListener(this);
                buttonOK.setOnClickListener(this);
                if (type == 0) {//只有确认
                    buttonNo.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(titleStr)) {
                    ((TextView) layoutView.findViewById(R.id.dialog_title)).setText(titleStr);
                }
                break;
            case R.layout.dialog_edite_common://普通 确定，取消(输入内容)
                layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edite_common, null);
                layoutView.findViewById(R.id.btn_no).setOnClickListener(this);
                layoutView.findViewById(R.id.btn_ok).setOnClickListener(this);
                commEditext = layoutView.findViewById(R.id.dialog_content);
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
