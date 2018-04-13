package com.renxin.doctor.activity.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.renxin.doctor.activity.R;

import java.text.MessageFormat;

/**
 * Created by Tang on 2017/11/9.
 */

public class UnbindDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private Button btn_no, btn_yes;

    private TextView tv_title;

    private String number;

    public UnbindDialog(@NonNull Activity context, String number,ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.number = number;
        this.mLisenter = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_unbind, null);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        tv_title = view.findViewById(R.id.id_tv_title);
        tv_title.setText(MessageFormat.format("是否解绑尾号{0}银行卡", number.substring(number.length() - 3, number.length())));
        setContentView(view);
        setCancelable(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.btn_ok:
                dismiss();
                if (mLisenter != null) {
                    mLisenter.confirmClicked(number);
                }
                break;
        }
    }

    public interface ClickListener {
        void confirmClicked(String number);
    }

    private ClickListener mLisenter;

}
