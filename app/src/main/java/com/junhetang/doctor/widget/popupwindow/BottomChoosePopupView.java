package com.junhetang.doctor.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.DrawableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BottomChoosePopupView  拍照，相册选择
 * Create at 2018/4/8 下午4:30 by mayakun
 */

public class BottomChoosePopupView extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    @BindView(R.id.dtv_one)
    DrawableTextView dtvOne;
    @BindView(R.id.dtv_two)
    DrawableTextView dtvTwo;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;

    private Context mContext;
    private String type;
    private View.OnClickListener mListener;

    /**
     * 默认拍照
     *
     * @param context
     * @param mListener
     */
    public BottomChoosePopupView(Context context, View.OnClickListener mListener) {
        this(context, BOTTOM_TYPE.TYPE_CAMERA, mListener);
    }

    /**
     * @param context
     * @param type
     * @param mListener
     */
    public BottomChoosePopupView(Context context, String type, View.OnClickListener mListener) {
        this.mContext = context;
        this.mListener = mListener;
        this.type = type;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_popup_view, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setContentView(view);

        ButterKnife.bind(this, view);

        switch (type) {
            case BOTTOM_TYPE.TYPE_CAMERA://拍照，相册
                break;
            case BOTTOM_TYPE.TYPE_OPEN_PAPER://在线开发，拍照开方
                dtvOne.setText("在线开方");
                dtvTwo.setText("拍照开方");
                dtvOne.setDrawable(DrawableTextView.LEFT, UIUtils.getDrawable(R.drawable.icon_online_center),
                        UIUtils.dp2px(mContext, 20), UIUtils.dp2px(mContext, 20));
                dtvTwo.setDrawable(DrawableTextView.LEFT, UIUtils.getDrawable(R.drawable.icon_camera_center),
                        UIUtils.dp2px(mContext, 20), UIUtils.dp2px(mContext, 20));
                break;
        }


        dtvOne.setOnClickListener(this);
        dtvTwo.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
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
            case R.id.dtv_one:
            case R.id.dtv_two:
                if (mContext != null) {
                    mListener.onClick(view);
                }
                break;
        }

    }

    //进来的类型
    public interface BOTTOM_TYPE {
        String TYPE_CAMERA = "type_camera";//拍照，相册
        String TYPE_OPEN_PAPER = "type_open_paper";//在线开方，拍照开方
    }
}
