package com.jht.doctor.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.jht.doctor.R;
import com.jht.doctor.utils.UIUtils;

/**
 * CameraPopupView  拍照，相册选择
 * Create at 2018/4/8 下午4:30 by mayakun
 */

public class CameraPopupView extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private Context mContext;
    private View.OnClickListener mListener;

    public CameraPopupView(Context context, View.OnClickListener mListener) {
        this.mContext = context;
        this.mListener = mListener;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.camera_bottom_view, null);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        view.findViewById(R.id.tv_cancle).setOnClickListener(this);
        view.findViewById(R.id.llt_camera).setOnClickListener(this);
        view.findViewById(R.id.llt_photo).setOnClickListener(this);
        this.setContentView(view);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(((Activity) mContext));
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onDismiss() {
        UIUtils.lightOn((Activity) mContext);
    }


    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_cancle:
                break;
            case R.id.llt_camera:
            case R.id.llt_photo:
                if (mContext != null) {
                    mListener.onClick(view);
                }
                break;
        }

    }
}
