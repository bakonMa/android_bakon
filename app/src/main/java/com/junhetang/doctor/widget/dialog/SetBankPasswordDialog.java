package com.junhetang.doctor.widget.dialog;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.ContributiveBean;
import com.junhetang.doctor.widget.PasswordInputView;

import java.text.MessageFormat;

/**
 * Created by Tang on 2017/11/9.
 */

public class SetBankPasswordDialog extends Dialog implements View.OnClickListener, TextWatcher {
    private Activity mContext;

    private Button btn_yes;

    private ImageView iv_close;

    private TextView tv_bankcard;

    private PasswordInputView inputView;

    private ContributiveBean mData;

    public SetBankPasswordDialog(@NonNull Activity context, ContributiveBean dataBean, ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.mData = dataBean;
        this.mListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_bankcard_password, null);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        iv_close = view.findViewById(R.id.id_iv_close);
        iv_close.setOnClickListener(this);
        tv_bankcard = view.findViewById(R.id.id_tv_bank);
        inputView = view.findViewById(R.id.id_ed_password);
        inputView.addTextChangedListener(this);
        if (mData != null) {
            String bankNo = mData.getBankCard();
            tv_bankcard.setText(MessageFormat.format("绑定{0}卡(尾号{1})",
                    mData.getBankName(), bankNo.substring(bankNo.length() - 4,
                            bankNo.length())));
        }
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
            case R.id.btn_ok:
                dismiss();
                //todo 确认使用并设置交易密码
                if (mListener != null) {
                    mListener.confirmClicked(mData, inputView.getText().toString());
                }
                break;
            case R.id.id_iv_close:
                dismiss();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btn_yes.setEnabled(s.length() >= 6);
    }

    public PasswordInputView getPswView() {
        return inputView;
    }

    public interface ClickListener {
        void confirmClicked(ContributiveBean contributiveBean, String psswrod);
    }

    private ClickListener mListener;

}
