package com.renxin.doctor.activity.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.UIUtils;
import com.renxin.doctor.activity.widget.PickerView;

import java.util.List;

/**
 * Created by Tang on 2017/10/30.
 */

public class TwoPopupWheel extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private TextView btn_cancel, btn_Comfirm, titleView;

    private PickerView pickerView1, pickerView2;

    private Activity mActivity;
    private String title;
    private List<String> mData1, mData2;


    public TwoPopupWheel(Activity activity, String title, List<String> mData1, List<String> mData2, ClickedListener listener) {
        super(activity);
        this.mActivity = activity;
        this.mData1 = mData1;
        this.mData2 = mData2;
        this.title = title;
        this.mListener = listener;
        initView();
        initEvent();
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

    private void initEvent() {
        pickerView1.setData(mData1);
        pickerView2.setData(mData2);
        pickerView1.setOnItemChangedListener(itemSelectedListerner);
    }

    private PickerView.ItemSelectedListerner itemSelectedListerner = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            pickerView2.setData(mData2);
        }
    };

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.double_wheel_view, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView = view.findViewById(R.id.id_title);
        btn_cancel = (TextView) view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = (TextView) view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);
        pickerView1 = (PickerView) view.findViewById(R.id.id_wheel1);
        pickerView2 = (PickerView) view.findViewById(R.id.id_wheel2);
        titleView.setText(TextUtils.isEmpty(title) ? "" : title);
    }

    @Override
    public void onDismiss() {
        UIUtils.lightOn(mActivity);
    }

    public void show(View parent) {
        UIUtils.lightOff(mActivity);
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    mListener.completeClicked(pickerView1.getPosition(), pickerView2.getPosition());
                }
                break;
        }
    }

    private ClickedListener mListener;

    public interface ClickedListener {
        void completeClicked(int pos1, int pos2);
    }
}
