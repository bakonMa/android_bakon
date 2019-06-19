package com.bakon.android.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bakon.android.R;


/**
 * 加载框
 */
public class LoadingDialog extends Dialog implements DialogInterface.OnDismissListener {

    private Context mContext;

    private ImageView imageView;

    private String mMessage;

    private TextView tv_message;

    private RelativeLayout rl_loading;

    private ImageView iv_error;

    private Animation animation;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    public LoadingDialog(Context context, String message) {
        super(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        mContext = context;
        mMessage = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = View.inflate(getContext(), R.layout.layout_loading, null);
        setContentView(view);
        iv_error = view.findViewById(R.id.id_iv_error);
        tv_message = view.findViewById(R.id.id_tv_message);
        rl_loading = view.findViewById(R.id.id_rl_loading);
        imageView = view.findViewById(R.id.id_image);
        setLoadingText(mMessage);
        setOnDismissListener(this);
        setCancelable(false);
    }

    public void setLoadingText(String showText) {
        if (!TextUtils.isEmpty(showText)) {
            tv_message.setText(showText);
        }
    }

    public void error(String errorMsg) {
        animation.cancel();
        rl_loading.setVisibility(View.GONE);
        iv_error.setVisibility(View.VISIBLE);
        tv_message.setText(errorMsg);
        handler.sendEmptyMessageDelayed(DISMISS, DISMISSDELAY);
    }

    public static final int DISMISS = 0x110;
    public static final long DISMISSDELAY = 1000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
            rl_loading.setVisibility(View.VISIBLE);
            iv_error.setVisibility(View.GONE);
            tv_message.setText("加载中...");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        animation = AnimationUtils.loadAnimation(
                mContext, R.anim.loading_anim);
        // 使用ImageView显示动画
        imageView.startAnimation(animation);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (animation != null) {
            animation.cancel();
        }
        if (handler != null && handler.hasMessages(DISMISS)) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
