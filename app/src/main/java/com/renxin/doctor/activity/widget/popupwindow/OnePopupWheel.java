package com.renxin.doctor.activity.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.UIUtils;
import com.renxin.doctor.activity.widget.PickerView;

import java.util.List;

/**
 * Created by Tang on 2017/10/30.
 */

public class OnePopupWheel extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private TextView btn_cancel, titleView, btn_Comfirm;

    private PickerView pickerView;

    private Activity mActivity;

    private List<String> mData;

    private String titel;

    private Listener mListener;

    public OnePopupWheel(Activity activity, List<String> datas, String title, Listener listener) {
        super(activity);
        this.mActivity = activity;
        this.mData = datas;
        this.mListener = listener;
        this.titel = title;
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

    public OnePopupWheel(Activity activity, List<String> datas, Listener listener) {
        this(activity, datas, "", listener);
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.wheel_view, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel = view.findViewById(R.id.id_btn_cancel);
        titleView = view.findViewById(R.id.id_title);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);
        pickerView = view.findViewById(R.id.id_wheel);
        pickerView.setData(mData);
        titleView.setText(TextUtils.isEmpty(titel) ? "" : titel);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(mActivity);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onDismiss() {
        UIUtils.lightOn(mActivity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_cancel:
                dismiss();
                break;
            case R.id.id_btn_comfirm:
                dismiss();
                if (mListener != null) {
                    mListener.completed(pickerView.getPosition());
                }
                break;
        }
    }

    public interface Listener {
        void completed(int position);
    }
}
