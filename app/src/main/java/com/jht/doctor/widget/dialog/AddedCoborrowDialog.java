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
import android.widget.TextView;

import com.jht.doctor.R;


/**
 * Created by Tang on 2017/11/9.
 */

public class AddedCoborrowDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private Button btn_yes;

    private TextView tv_title;

    public static final int PELEASE_ADD_MAIN_CARD = 0x110; //请先添加主借人银行卡
    public static final int YOU_HAVE_ALREADY_ADD = 0x111;   //你已经添加过这个共借人了
    public static final int HAVE_REJECTED = 0x112;          //此订单已被拒绝
    private int type;


    public AddedCoborrowDialog(@NonNull Activity context, int type, ConfirmListenter confirmListenter) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.mListener = confirmListenter;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_added_coborrower, null);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        tv_title = view.findViewById(R.id.id_tv_title);
        switch (type) {
            case PELEASE_ADD_MAIN_CARD:
                tv_title.setText("请先添加主借人银行卡");
                break;
            case YOU_HAVE_ALREADY_ADD:
                tv_title.setText("该共借人已存在");
                break;
            case HAVE_REJECTED:
                tv_title.setText("此订单已被拒绝");
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
                if (mListener != null){
                    mListener.confirmClicked();
                }
                break;
        }
    }

    private ConfirmListenter mListener;

    public interface ConfirmListenter {
        void confirmClicked();
    }

}
