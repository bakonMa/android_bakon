package com.jht.doctor.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.jht.doctor.R;
import com.jht.doctor.utils.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * Created by wy7217 on 2017/11/2.
 *
 * @author wy7217
 */

public class SharePopupWindow extends PopupWindow {


    private final WeakReference<Context> context;
    private View view;

    public SharePopupWindow(Context context) {
        super(context);
        this.context = new WeakReference<>(context);
        init();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        ScreenUtils.lightOff(((Activity) context.get()));
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init() {
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        setAnimationStyle(R.style.bottom_pop);
//        view = LayoutInflater.from(context.get()).inflate(R.layout.popup_share, null);
//        setContentView(view);
//        setOnDismissListener(() -> ScreenUtils.popupWindowDismissShadow((Activity) context.get()));
    }

    public SharePopupWindow setListener(final ShareOnClickListener shareOnClickListener) {
        //微信好友
//        view.findViewById(R.id.rl_popup_share_wechat).setOnClickListener(v -> {
//            if (shareOnClickListener != null) {
//                shareOnClickListener.onItemClick(Constant.ShareType.WeChat);
//            }
//        });

        return this;
    }

    public interface ShareOnClickListener {
        void onItemClick(int shareType);
        void cancleOnClick();
    }
}
