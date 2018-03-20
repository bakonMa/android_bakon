package com.jht.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.widget.PasswordInputView;


/**
 * Created by Tang on 2017/11/9.
 */

public class SetPasswordDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private Activity mContext;

    private RelativeLayout btn_close;

    private PasswordInputView mPassword;

    private TextView btn_confirm;


    public SetPasswordDialog(@NonNull Activity context, ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mListener = clickListener;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_password, null);
        btn_close = view.findViewById(R.id.id_rl_close);
        btn_close.setOnClickListener(this);
        mPassword = view.findViewById(R.id.id_ed_password);
        mPassword.addTextChangedListener(this);
        btn_confirm = view.findViewById(R.id.id_btn_confirm);
        btn_confirm.setOnClickListener(this);
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
            case R.id.id_rl_close:
                dismiss();
                break;
            case R.id.id_btn_confirm:
                dismiss();
                if (mListener != null) {
                    mListener.confirmClick(mPassword.getText().toString());
                }
                break;
        }
    }

    public PasswordInputView getPswView() {
        return mPassword;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        btn_confirm.setEnabled(editable.length() >= 6);
    }

    private ClickListener mListener;

    public interface ClickListener {
        void confirmClick(String password);
    }
}
