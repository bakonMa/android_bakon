package com.jht.doctor.ui.activity.repayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.bankcard.MyBankCardActivity;
import com.jht.doctor.ui.activity.mine.bankcard.SupportBankActivity;
import com.jht.doctor.ui.activity.mine.setting.SettingActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.PasswordInputView;
import com.jht.doctor.widget.dialog.EnterPasswordDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * mayakun
 * 提现
 */
public class WithdrawCashActivity extends BaseAppCompatActivity implements RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_name)
    TextView idTvName;
    @BindView(R.id.id_tv_bank_card)
    TextView idTvBankCard;
    @BindView(R.id.id_ed_withdraw_money)
    EditText idEdWithdrawMoney;
    @BindView(R.id.id_btn_withdraw_all)
    TextView idBtnWithdrawAll;
    @BindView(R.id.id_btn_withdraw)
    TextView idBtnWithdraw;
    @Inject
    RepaymentPresenter mPresenter;

    private String orderNo, name, phone, idNum, platformUserNo;
    private double ownAmt;//课提现金额
    private EnterPasswordDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cash);
        ButterKnife.bind(this);
        orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        initToolbar();
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        idNum = getIntent().getStringExtra("idnum");
        ownAmt = getIntent().getDoubleExtra("ownAmt", 0d);
        platformUserNo = getIntent().getStringExtra("platformUserNo");


        idTvName.setText(TextUtils.isEmpty(name) ? "" : RegexUtil.hideFirstName(name));
        idTvBankCard.setText(TextUtils.isEmpty(idNum) ? "" : RegexUtil.hideBankCardId(idNum));
        idEdWithdrawMoney.setHint(String.format("当前可提现%s元", RegexUtil.formatDoubleMoney(ownAmt)));
    }

    String POINT = ".";

    @OnTextChanged(value = R.id.id_ed_withdraw_money, callback = OnTextChanged.Callback.TEXT_CHANGED)
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
                idEdWithdrawMoney.setText(s);
                idEdWithdrawMoney.setSelection(idEdWithdrawMoney.getText().length());
            }
        }
        idBtnWithdraw.setEnabled(!TextUtils.isEmpty(s) && Double.parseDouble(s.toString()) > 0);
    }

    PasswordInputView passwordInputView;

    @OnClick({R.id.id_btn_withdraw, R.id.id_btn_withdraw_all})
    void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_withdraw_all:
                idEdWithdrawMoney.setText(RegexUtil.formatDoubleMoney(ownAmt));
                idEdWithdrawMoney.setSelection(idEdWithdrawMoney.getText().length());
                break;
            case R.id.id_btn_withdraw:
                double money = Double.parseDouble(idEdWithdrawMoney.getText().toString().trim());
                if (money < 5) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("提现金额不可小于5元，请重新输入");
                    idEdWithdrawMoney.setText("");
                    return;
                } else if (money > ownAmt) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("提现金额大于当前可提现金额，请重新输入");
                    idEdWithdrawMoney.setText("");
                    return;
                }
                //获取交易方式（验证码/交易密码）
                mPresenter.getPayWay(orderNo);
                break;
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("提现")
                .setLeft(false)
                .setRightText("限额说明", true, R.color.tab_selected)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        Intent intent = new Intent(provideContext(), SupportBankActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        startActivity(intent);
                    }
                }).bind();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (dialog != null && !TextUtils.isEmpty(errorMsg)) {
            dialog.setErrorText(errorMsg);
            passwordInputView.setText("");
        } else {
            CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }

    }

    @Override
    public void onSuccess(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        if (dialog != null) {
            dialog.dismiss();
        }

        switch (message.what) {
            case RepaymentPresenter.REPAYMENT_SMS_CODE://调用验证码 成功进入验证码
                //跳转到验证码页面
                Intent intentCode = new Intent(this, RepaymentVerifyCodeActivity.class);
                intentCode.putExtra("phone", phone);
                intentCode.putExtra("orderNo", orderNo);
                intentCode.putExtra("type", RepaymentPresenter.TYPE_WITHDRAW);//提现
                intentCode.putExtra("platformUserNo", platformUserNo);
                intentCode.putExtra("amount", Double.parseDouble(idEdWithdrawMoney.getText().toString().trim()));
                startActivity(intentCode);
                break;
            case RepaymentPresenter.REPAYMENT_WITHDRAW://交易密码 完成
                Intent intent = new Intent(this, WithdrawSuccessActivity.class);
                intent.putExtra("amount", Double.parseDouble(idEdWithdrawMoney.getText().toString()));
                startActivity(intent);
                //通知我的账户刷新数据
                EventBusUtil.sendEvent(new Event(EventConfig.WITHDRAW_OK));
                finish();
                break;
            case RepaymentPresenter.REPAYMENT_PAYWAY://支付方式
                OtherBean bean = (OtherBean) message.obj;
                if ("01".equals(bean.withdrawValidate)) {//短信验证码方式
                    mPresenter.getSmsVerifyCode(0, RepaymentPresenter.TYPE_WITHDRAW,
                            platformUserNo, orderNo, idEdWithdrawMoney.getText().toString());
                } else if ("02".equals(bean.withdrawValidate)) {//交易密码方式
                    dialog = new EnterPasswordDialog(this, new EnterPasswordDialog.EnterPassWordCallback() {
                        @Override
                        public void somethingDone(View view) {
                            switch (view.getId()) {
                                case R.id.id_btn_forgive_password:
                                    startActivity(new Intent(provideContext(), SettingActivity.class));
                                    dialog.dismiss();
                                    break;
                                case R.id.id_ed_password:
                                    // 密码支付
                                    String pwd = ((PasswordInputView) view).getText().toString();
                                    Params params = new Params();
                                    params.put("amount", idEdWithdrawMoney.getText().toString().trim());
                                    params.put("orderNo", orderNo);
                                    params.put("otherPlatformId", platformUserNo);
                                    params.put("pwd", pwd);
                                    mPresenter.pwdWithDraw(params);
                                    break;
                            }
                        }
                    });

                    dialog.show();
                    passwordInputView = dialog.findViewById(R.id.id_ed_password);
                    KeyBoardUtils.showKeyBoard(passwordInputView, provideContext());
                }
                break;
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

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                //提现成功 关闭页面
                case EventConfig.WITHDRAW_OK:
                    finish();
                    break;
            }
        }
    }
}
