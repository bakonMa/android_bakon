package com.jht.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;


/**
 * Created by Tang on 2017/11/9.
 */

public class OngoingDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout idRlClose;
    private TextView idBtnContinue;
    private Activity mContext;

    private TextView label1, label2;
    public static final int ONGOING = 0x110;
    public static final int REPAYMENT = 0x112;
    private int mTag;


    public OngoingDialog(@NonNull Activity context, int tag) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.mTag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_on_going, null);
        idRlClose = view.findViewById(R.id.id_rl_close);
        idRlClose.setOnClickListener(this);
        idBtnContinue = view.findViewById(R.id.id_btn_continue);
        idBtnContinue.setOnClickListener(this);
        label1 = view.findViewById(R.id.id_tv_label1);
        label2 = view.findViewById(R.id.id_tv_label2);
        initData();
        setContentView(view);
        setCancelable(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    private void initData() {
        switch (mTag) {
            case ONGOING:
                label1.setText("您有一笔订单进行中");
                label2.setText("是否继续完成");
                idBtnContinue.setText("继续完成");
                break;
            case REPAYMENT:
                label1.setText("您有一笔借款正在还款中");
                label2.setText("暂不支持继续申请");
                idBtnContinue.setText("查看已有借款");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_rl_close:
                dismiss();
                break;
            case R.id.id_btn_continue:
                switch (mTag){
                    case ONGOING:
//                        mContext.startActivity(new Intent(mContext, MyLoanListActivity.class));
                        break;
                    case REPAYMENT:
//                        mContext.startActivity(new Intent(mContext, HomeRepaymentActivity.class));
                        break;
                }
                dismiss();
                break;
        }
    }
}
