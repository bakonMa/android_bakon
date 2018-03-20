package com.jht.doctor.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.widget.PickerView;

import java.util.List;

/**
 * Created by Tang on 2017/10/30.
 */

public class OnePopupWheel extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private TextView btn_cancel, btn_Comfirm;

    private PickerView pickerView;

    private Activity mActivity;

    private List<String> mData;

    private Listener mListener;

    public OnePopupWheel(Activity activity, List<String> datas, Listener listener) {
        super(activity);
        this.mActivity = activity;
        this.mData = datas;
        this.mListener = listener;
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
        this.setOnDismissListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.wheel_view, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel = (TextView) view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = (TextView) view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);
        pickerView = (PickerView) view.findViewById(R.id.id_wheel);
        pickerView.setData(mData);
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
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
