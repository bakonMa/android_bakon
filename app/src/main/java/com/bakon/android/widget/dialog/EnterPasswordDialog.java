package com.bakon.android.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.widget.PasswordInputView;


/**
 * Created by Tang on 2017/11/9.
 */

public class EnterPasswordDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private Activity mContext;

    private TextView tv_error, btn_forget;
    private RelativeLayout btn_close;
    private PasswordInputView mPassword;

    private EnterPassWordCallback callback;


    public EnterPasswordDialog(@NonNull Activity context, EnterPassWordCallback callback) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_withdraw_cash, null);
        btn_close = view.findViewById(R.id.id_rl_close);
        btn_close.setOnClickListener(this);
        btn_forget = view.findViewById(R.id.id_btn_forgive_password);
        btn_forget.setOnClickListener(this);
        tv_error = view.findViewById(R.id.id_tv_error_messgae);
        mPassword = view.findViewById(R.id.id_ed_password);
        mPassword.addTextChangedListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
        setContentView(view);
        setCancelable(false);
    }


    @Override
    public void onClick(View view) {
        if (callback == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.id_rl_close:
                dismiss();
                break;
            default:
                callback.somethingDone(view);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() >= 6) {
            callback.somethingDone(mPassword);
//            mContext.startActivity(new Intent(mContext, WithdrawSuccessActivity.class));
        }
    }

    //显示错误信息
    public void setErrorText(String errorText) {
        tv_error.setText(TextUtils.isEmpty(errorText) ? "" : errorText);
    }

    //回调接口
    public interface EnterPassWordCallback {
        void somethingDone(View view);
    }

}
