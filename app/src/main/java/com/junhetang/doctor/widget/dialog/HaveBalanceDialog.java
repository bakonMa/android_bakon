package com.junhetang.doctor.widget.dialog;

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

import com.junhetang.doctor.R;


/**
 * Created by Tang on 2017/11/9.
 */

public class HaveBalanceDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private Button btn_no, btn_yes;

    public static final int HAVE_BALANCE = 0x110;//账户还有余额
    public static final int LOG_OUT = 0x111;   //退出确认

    private int type;

    public HaveBalanceDialog(@NonNull Activity context, int type, ClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.type = type;
        this.clickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_have_balance, null);
        TextView textView = view.findViewById(R.id.id_tv_title);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        switch (type) {
            case HAVE_BALANCE:
                btn_yes.setText("去提现");
                textView.setText("您的账户还有余额，请提现后再试");
                break;
            case LOG_OUT:
                btn_yes.setText("确认");
                textView.setText("确认退出当前账号？");
                break;
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
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.btn_ok:
                dismiss();
                if (clickListener != null) {
                    clickListener.confirm();
                }
                break;
        }
    }

    public interface ClickListener {
        void confirm();
    }

    private ClickListener clickListener;

}
