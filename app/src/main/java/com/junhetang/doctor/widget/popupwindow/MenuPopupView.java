package com.junhetang.doctor.widget.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.UIUtils;


/**
 * MenuPopupView 菜单popupwindow
 * Create at 2018/5/3 下午5:38 by mayakun
 */
public class MenuPopupView extends PopupWindow implements View.OnClickListener {
    private Context mContext;

    private TextView tv_share, tv_save;

    public MenuPopupView(Context context, OnClickListener onClickListener) {
        this.mContext = context;
        this.mListener = onClickListener;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Widget_AppCompat_PopupMenu_Overflow);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popuplewindow_menu, null);
        this.setContentView(view);
        this.setWidth(UIUtils.dp2px(mContext, 150));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_share = view.findViewById(R.id.tv_share);
        tv_save = view.findViewById(R.id.tv_save);
        tv_share.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (mListener != null) {
            dismiss();
            mListener.onClicked(view);
        }
    }

    //显示
    public void show(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, UIUtils.dp2px(mContext, xoff), UIUtils.dp2px(mContext, yoff));
    }

    public void show(View anchor) {
        super.showAsDropDown(anchor);
    }

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClicked(View view);
    }

}
