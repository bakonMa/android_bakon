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
import android.widget.EditText;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.ToastUtil;


/**
 * SavePaperDialog  保存常用处方
 * Create at 2018/4/28 下午4:38 by mayakun
 */
public class SavePaperDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private ClickListener clickListener;
    private Button btn_no, btn_yes;
    private EditText etName, etRemark;

    public SavePaperDialog(@NonNull Activity context, ClickListener listener) {
        super(context, R.style.common_dialog);
        this.mContext = context;
        this.clickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_save_commpaper, null);
        etName = view.findViewById(R.id.et_papername);
        etRemark = view.findViewById(R.id.et_remark);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        setContentView(view);
        setCancelable(false);
        //位置，大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    //设置显示之前的内容
    public void setEditeText(String title, String explain) {
        etName.setText(TextUtils.isEmpty(title) ? "" : title);
        etName.setSelection(etName.getText().length());
        etRemark.setText(TextUtils.isEmpty(explain) ? "" : explain);
        etRemark.setSelection(etRemark.getText().length());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.btn_ok:
                if (clickListener != null) {
                    if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                        ToastUtil.showShort("请输入处方名称");
                        return;
                    }
                    clickListener.confirm(etName.getText().toString().trim(), etRemark.getText().toString().trim());
                }
                break;
        }
    }

    public interface ClickListener {
        void confirm(String name, String remark);
    }


}
