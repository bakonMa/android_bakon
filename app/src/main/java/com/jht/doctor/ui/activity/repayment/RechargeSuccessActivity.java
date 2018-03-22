package com.jht.doctor.ui.activity.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.SessionMapKey;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.MyAccountInfoBean;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeSuccessActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_money)
    TextView idTvMoney;
    @BindView(R.id.id_btn_comfirm)
    TextView idBtnComfirm;
    //金额
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_success);
        ButterKnife.bind(this);
        initToolbar();
        amount = getIntent().getDoubleExtra("amount", 0d);
        idTvMoney.setText(RegexUtil.formatMoney(amount));
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .blank()
                .setStatuBar(R.color.white)
                .bind();
    }

    @Override
    protected void setupActivityComponent() {

    }

    @OnClick(R.id.id_btn_comfirm)
    public void onViewClicked() {
        MyAccountInfoBean.UserAccountInfoDTOBean bean = (MyAccountInfoBean.UserAccountInfoDTOBean) DocApplication.getAppComponent().sessionMap().get(SessionMapKey.RECHAGE_BEAN);
        if (bean == null) {
            finish();
        } else {
            Intent intent = new Intent(this, TradeDetailActivity.class);
            intent.putExtra("name", bean.getUserName());
            intent.putExtra("nameType", bean.getUserRole());
            intent.putExtra("bankNum", bean.getUserBankNo());
            intent.putExtra("platformUserNo", bean.getPlatformUserNo());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
