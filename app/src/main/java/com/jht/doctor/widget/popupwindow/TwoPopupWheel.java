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

public class TwoPopupWheel extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private TextView btn_cancel, btn_Comfirm;

    private PickerView pickerView1, pickerView2;

    private Activity mActivity;

    private List<String> mData1,mData2;


    public TwoPopupWheel(Activity activity, List<String> repaymentMethods,List<String> period, ClickedListener listener) {
        super(activity);
        this.mActivity = activity;
        this.mData1 = repaymentMethods;
        this.mData2 = period;
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
        btn_cancel = (TextView) view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = (TextView) view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);
        pickerView1 = (PickerView) view.findViewById(R.id.id_wheel1);
        pickerView2 = (PickerView) view.findViewById(R.id.id_wheel2);
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
                    mListener.completeClicked(pickerView1.getPosition(),pickerView2.getPosition());
                }
                break;
        }
    }

    private ClickedListener mListener;

    public interface ClickedListener {
        void completeClicked(int pos1,int pos2);
    }
}
