package com.junhetang.doctor.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junhetang.doctor.R;


/**
 * Created by Tang on 2017/11/15.
 */

public class BottomPopupView extends PopupWindow implements View.OnClickListener {
    private Context mContext;

    private TextView tv_remain;

    private String money;

    public BottomPopupView(Context context, String money, OnClickListener onClickListener) {
        this.mContext = context;
        this.money = money;
        this.mListener = onClickListener;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_view, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_remain = view.findViewById(R.id.id_tv_remain_money);
        view.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (mListener != null){
            mListener.onClicked();
        }
    }

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClicked();
    }

}
