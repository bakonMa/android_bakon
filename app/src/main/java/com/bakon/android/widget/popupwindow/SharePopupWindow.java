package com.bakon.android.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bakon.android.R;
import com.bakon.android.utils.UIUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

/**
 * SharePopupWindow
 * Create at 2018/4/12 下午3:22 by mayakun
 */

public class SharePopupWindow extends PopupWindow {

    private final WeakReference<Context> context;
    private View view;
    private ShareOnClickListener listener;

    public SharePopupWindow(Context context, ShareOnClickListener listener) {
        super(context);
        this.listener = listener;
        this.context = new WeakReference<>(context);
        init();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(((Activity) context.get()));
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init() {
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setAnimationStyle(R.style.dir_popupwindow_anim);
        view = LayoutInflater.from(context.get()).inflate(R.layout.popup_share, null);
        setContentView(view);
        setOnDismissListener(() -> UIUtils.lightOn((Activity) context.get()));
        if (listener != null) {
            setListener(listener);
        }
    }

    public SharePopupWindow setListener(final ShareOnClickListener shareOnClickListener) {
        //微信好友
        view.findViewById(R.id.rl_popup_share_wechat).setOnClickListener(v -> {
            dismiss();
            if (shareOnClickListener != null) {
                shareOnClickListener.onItemClick(SHARE_MEDIA.WEIXIN);
            }
        });
        //微信朋友圈
        view.findViewById(R.id.rl_popup_share_wechat_circle).setOnClickListener(v -> {
            dismiss();
            if (shareOnClickListener != null) {
                shareOnClickListener.onItemClick(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        //QQ好友
        view.findViewById(R.id.rl_popup_share_qqfriends).setOnClickListener(v -> {
            dismiss();
            if (shareOnClickListener != null)
                shareOnClickListener.onItemClick(SHARE_MEDIA.QQ);
        });
        //取消按钮
        view.findViewById(R.id.tv_popup_share_cancle).setOnClickListener(v -> {
            dismiss();
        });
        return this;
    }

    public interface ShareOnClickListener {
        void onItemClick(SHARE_MEDIA shareType);
    }
}
