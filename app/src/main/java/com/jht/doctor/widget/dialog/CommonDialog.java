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
import android.widget.TextView;

import com.jht.doctor.R;


/**
 * Created by mayakun on 2017/12/6.
 */

public class CommonDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private int layout;
    private String titleStr;

    private View layoutView;
    private View.OnClickListener listener;

    public CommonDialog(@NonNull Activity context, int layout, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.layout = layout;
        this.listener = listener;
    }

    public CommonDialog(@NonNull Activity context, int layout, String titleStr, View.OnClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.layout = layout;
        this.titleStr = titleStr;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        switch (layout) {
            case R.layout.dialog_my_info://普通 确定，取消
                layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_my_info, null);
                layoutView.findViewById(R.id.btn_no).setOnClickListener(this);
                layoutView.findViewById(R.id.btn_ok).setOnClickListener(this);
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
        listener.onClick(view);
        dismiss();
    }

}
