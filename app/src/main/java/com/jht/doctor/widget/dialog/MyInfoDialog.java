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

import com.jht.doctor.R;


/**
 * Created by Tang on 2017/11/9.
 */

public class MyInfoDialog extends Dialog implements View.OnClickListener{
    private Activity mContext;

    private Button btn_no,btn_yes;


    public MyInfoDialog(@NonNull Activity context) {
        super(context, R.style.common_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_my_info, null);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
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
                break;
        }
    }

}
