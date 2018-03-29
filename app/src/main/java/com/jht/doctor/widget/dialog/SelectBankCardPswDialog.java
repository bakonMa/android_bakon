package com.jht.doctor.widget.dialog;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.ui.bean.ContributiveBean;
import com.jht.doctor.utils.ToastUtil;

import java.text.MessageFormat;

/**
 * Created by Tang on 2017/11/9.
 */

public class SelectBankCardPswDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private Button btn_yes;

    private TextView tv_bankcard;

    private CheckBox checkBox;

    private ImageView iv_close;

    private ContributiveBean mData;

    public SelectBankCardPswDialog(@NonNull Activity context, ContributiveBean contributiveBean, ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.mData = contributiveBean;
        this.mListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_bankcard_psw, null);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        tv_bankcard = view.findViewById(R.id.id_tv_bank_card);
        iv_close = view.findViewById(R.id.id_iv_close);
        iv_close.setOnClickListener(this);
        if (mData != null) {
            String bankNo = mData.getBankCard();
            tv_bankcard.setText(MessageFormat.format("{0}（尾号{1}）",
                    mData.getBankName(), bankNo.substring(bankNo.length() - 4,
                            bankNo.length())));
        }
        checkBox = view.findViewById(R.id.id_cb);
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
                //todo 确认使用
                if (!checkBox.isChecked()) {
                    ToastUtil.show("请选中要使用的银行卡");
                } else {
                    if (mListener != null) {
                        mListener.confirmClicked(mData);
                    }
                }
                break;
            case R.id.id_iv_close:
                dismiss();
                if (mListener != null) {
                    mListener.closed();
                }
                break;
        }
    }

    public interface ClickListener {
        void confirmClicked(ContributiveBean contributiveBean);

        void closed();
    }

    private ClickListener mListener;

}
