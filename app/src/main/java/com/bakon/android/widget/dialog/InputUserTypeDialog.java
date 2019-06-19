package com.bakon.android.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.utils.ToastUtil;

import java.text.MessageFormat;


/**
 * InputUserTypeDialog 自定义用法
 * Create at 2018/12/6 下午1:09 by mayakun
 */
public class InputUserTypeDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;

    private ClickListener clickListener;
    private Button btn_no, btn_yes;
    private EditText etContent;
    private TextView tvCount;

    public InputUserTypeDialog(@NonNull Activity context, ClickListener listener) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_usetype, null);
        etContent = view.findViewById(R.id.et_content);
        tvCount = view.findViewById(R.id.tv_count);
        btn_no = view.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(this);
        btn_yes = view.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(this);
        setContentView(view);
        setCancelable(true);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();
                tvCount.setText(MessageFormat.format("{0}/10", len));
            }
        });
        //位置，大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    //设置显示之前的内容
    public void setEditeText(String title) {
        etContent.setText(TextUtils.isEmpty(title) ? "" : title);
        etContent.setSelection(etContent.getText().length());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_no:
                dismiss();
                break;
            case R.id.btn_ok:
                if (clickListener != null) {
                    if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                        ToastUtil.showShort("请输入用法自定义名称");
                        return;
                    }
                    clickListener.confirm(etContent.getText().toString().trim());
                    dismiss();
                }
                break;
        }
    }

    public interface ClickListener {
        void confirm(String name);
    }


}
