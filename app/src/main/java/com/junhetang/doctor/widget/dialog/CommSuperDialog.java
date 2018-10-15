package com.junhetang.doctor.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.junhetang.doctor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CommSuperDialog 添加患者，结果dialog
 * Create at 2018/9/3 下午2:05 by mayakun
 */
public class CommSuperDialog extends Dialog {

    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_right)
    Button btnRight;

    private ClickListener mListener;

    private Activity mContext;
    private int uiType = 0;
    private boolean isShowDouble = true;
    private String content, leftStr, rightStr;

    /**
     * 默认【取消，确定】
     *
     * @param context
     * @param clickListener
     */
    public CommSuperDialog(@NonNull Activity context, String content, ClickListener clickListener) {
        this(context, content, 0, true, "取  消", "确  定", clickListener);
    }
    /**
     * 【确定】带ClickListener
     *
     * @param context
     * @param clickListener
     */
    public CommSuperDialog(@NonNull Activity context, boolean isShowDouble, String content, ClickListener clickListener) {
        this(context, content, 0, false, "", "确  定", clickListener);
    }

    /**
     * 默认【确定】只有一个
     *
     * @param context
     */
    public CommSuperDialog(@NonNull Activity context, String content) {
        this(context, content, 0, false, "", "确  定", null);
    }

    /**
     * 自定义【关闭***，详情***】按钮文字
     *
     * @param context
     * @param clickListener
     */
    public CommSuperDialog(@NonNull Activity context, String content, String leftStr, String rightStr, ClickListener clickListener) {
        this(context, content, 0, true, leftStr, rightStr, clickListener);
    }


    public CommSuperDialog(@NonNull Activity context, String content, int uiType, boolean isShowDouble,
                           String leftStr, String rightStr, ClickListener clickListener) {
        super(context, R.style.common_dialog);
        this.mListener = clickListener;
        this.mContext = context;
        this.content = content;
        this.uiType = uiType;
        this.isShowDouble = isShowDouble;
        this.leftStr = TextUtils.isEmpty(leftStr) ? "取  消" : leftStr;
        this.rightStr = TextUtils.isEmpty(rightStr) ? "确  定" : rightStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comm_super, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        dialogTitle.setText(TextUtils.isEmpty(content) ? "" : content);
        //后一个 按钮是否显示
        btnLeft.setVisibility(isShowDouble ? View.VISIBLE : View.GONE);
        //按钮文字
        btnLeft.setText(leftStr);
        btnRight.setText(rightStr);
        switch (uiType) {
            case 0://默认样式
                break;
            case 1://左右都是用主题色
                btnLeft.setBackgroundResource(R.drawable.selector_bg_button_login);
                btnRight.setBackgroundResource(R.drawable.selector_bg_button_login);
                break;
        }

        setCancelable(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }


    @OnClick({R.id.btn_left, R.id.btn_right})
    void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
            case R.id.btn_left:
                dismiss();
                if (mListener != null) {
                    mListener.btnOnClick(view.getId());
                }
                break;
        }
    }

    //修改dialog文字
    public void setDialogTitle(String msg){
        dialogTitle.setText(TextUtils.isEmpty(msg) ? "" : msg);
    }

    public interface ClickListener {
        void btnOnClick(int btnId);
    }
}
