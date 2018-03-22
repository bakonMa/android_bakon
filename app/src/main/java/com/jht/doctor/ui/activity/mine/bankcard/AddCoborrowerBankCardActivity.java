package com.jht.doctor.ui.activity.mine.bankcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCoborrowerBankCardActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_edit_bankcard)
    EditTextlayout idEditBankcard;
    @BindView(R.id.id_tv_bankname)
    TextView idTvBankname;
    @BindView(R.id.id_edit_phone)
    EditTextlayout idEditPhone;
    @BindView(R.id.id_btn_confirm)
    TextView idBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coborrower_bank_card);
        ButterKnife.bind(this);
        initToolbar();
        initEvent();
    }

    private void initEvent() {
        idEditBankcard.getEditText().addTextChangedListener(textWatcherImp);
        idEditPhone.getEditText().addTextChangedListener(textWatcherImp);
    }

    private TextWatcher textWatcherImp = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            idBtnConfirm.setEnabled(idEditBankcard.getEditText().getText().toString().length() > 0
                    && idEditPhone.getEditText().getText().toString().length() > 0);
        }
    };

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("添加共借人银行卡")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("支持银行", true, R.color.color_4f9ef3)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();

                    }
                }).bind();

    }

    @Override
    protected void setupActivityComponent() {

    }

    @OnClick(R.id.id_btn_confirm)
    public void onViewClicked() {
        startActivity(new Intent(this,AddBankCardVerifyActivity.class));
    }
}
