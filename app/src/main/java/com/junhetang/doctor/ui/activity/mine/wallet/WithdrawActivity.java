package com.junhetang.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BankCardBean;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.ui.presenter.WalletPresenter;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * WithdrawActivity  提现
 * Create at 2018/4/11 上午10:30 by mayakun
 */
public class WithdrawActivity extends BaseActivity implements WalletContact.View {

    public static int REQUEST_CODE_SELECTBANKCARD = 20001;//选择银行卡

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_cardbank)
    TextView tvCardbank;
    @BindView(R.id.tv_cardnum)
    TextView tvCardnum;
    @BindView(R.id.et_inputmoney)
    EditText etInputmoney;
    @BindView(R.id.tv_canusemoney)
    TextView tvCanusemoney;
    @BindView(R.id.btn_complete)
    Button btnComplete;

    @Inject
    WalletPresenter mPresenter;

    private String canUseMoney;
    private BankCardBean cardBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        initToolbar();
        cardBean = getIntent().getParcelableExtra("bankcard");
        canUseMoney = getIntent().getStringExtra("remain");

        if (null != cardBean) {
            tvCardbank.setText(TextUtils.isEmpty(cardBean.ch_name) ? "" : cardBean.ch_name);
            tvCardnum.setText(TextUtils.isEmpty(cardBean.bank_number) ? "" : cardBean.bank_number);
        }
        if (TextUtils.isEmpty(canUseMoney)) {
            canUseMoney = "0.00";
        }
        tvCanusemoney.setText("可提现余额" + canUseMoney + "元");
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("钱包")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }
    
    String POINT = ".";

    @OnTextChanged(value = R.id.et_inputmoney, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void editeChanged(Editable editable) {
        //解决显示的问题
        String s = editable.toString();
        if (!TextUtils.isEmpty(s) && s.toString().contains(POINT)) {
            boolean mustBeChange = false;
            if (s.startsWith(POINT)) {// .  |  .01 这个要重置et的值
                s = "0" + s;
                mustBeChange = true;
            } else if (!s.endsWith(POINT) && s.indexOf(POINT) + 2 < s.length() - 1) { // 10.234 这个是重置et值
                s = s.substring(0, s.toString().indexOf(POINT) + 2 + 1);
                mustBeChange = true;
            }
            if (mustBeChange) {
                etInputmoney.setText(s);
                etInputmoney.setSelection(etInputmoney.getText().length());
            }
        }
        btnComplete.setEnabled(s.length() > 0
                && Double.parseDouble(s.toString()) > 0
                && Double.parseDouble(s.toString()) <= Double.parseDouble(canUseMoney));
    }

    @OnClick({R.id.rlt_banklist, R.id.tv_userall, R.id.btn_complete})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_banklist:
                Intent intent = new Intent(this, MyBankCardActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE_SELECTBANKCARD);
                break;
            case R.id.tv_userall:
                etInputmoney.setText(canUseMoney);
                etInputmoney.setSelection(canUseMoney.length());
                break;
            case R.id.btn_complete:
                CommonDialog commonDialog = new CommonDialog(this, false, "确认提现", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btn_ok) {
                            mPresenter.witdraw(cardBean.id, etInputmoney.getText().toString().trim());
                        }
                    }
                });
                commonDialog.show();
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case WalletPresenter.WITHDRAW_OK:
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_WITHRAW_OK));
                ToastUtil.showShort("提现请求已发送成功");
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECTBANKCARD) {//选择银行卡
            cardBean = data.getParcelableExtra("bankcard");
            if (cardBean != null) {
                tvCardbank.setText(TextUtils.isEmpty(cardBean.ch_name) ? "" : cardBean.ch_name);
                tvCardnum.setText(TextUtils.isEmpty(cardBean.bank_number) ? "" : cardBean.bank_number);
            }
        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

}
