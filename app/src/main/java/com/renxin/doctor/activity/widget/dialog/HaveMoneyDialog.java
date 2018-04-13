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


/**
 * Created by Tang on 2017/11/9.
 */

public class HaveMoneyDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;

    private Button btn_yes;

    private TextView tv_label1,tv_label2;

    private int type;

    public static final int HAVE_MONEY = 0x110;//您的账户还有在途资金
    public static final int NOT_SAME = 0x111;   //共借人信息不能与主借人相同


    public HaveMoneyDialog(@NonNull Activity context,int type) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_have_money, null);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        tv_label1 = view.findViewById(R.id.id_tv_label1);
        tv_label2 = view.findViewById(R.id.id_tv_label2);
        switch (type){
            case HAVE_MONEY:
                tv_label1.setText("您的账户还有在途资金");
                tv_label2.setText("请稍后再试");
                break;
            case NOT_SAME:
                tv_label1.setText("共借人信息不能与主借人相同");
                tv_label2.setText("请重新添加");
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
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }

}
