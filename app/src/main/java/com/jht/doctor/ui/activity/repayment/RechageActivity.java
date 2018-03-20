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
 * 充值
 */
public class RechageActivity extends BaseAppCompatActivity implements RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_name)
    TextView idTvName;
    @BindView(R.id.id_tv_bank_card)
    TextView idTvBankCard;
    @BindView(R.id.id_ed_recharge_money)
    EditText idEdRechargeMoney;
    @BindView(R.id.id_btn_recgarge)
    TextView idBtnRecgarge;
    @Inject
    RepaymentPresenter mPresenter;

    private String orderNo, name, idNum, phone;
    private String platformUserNo;//平台用户账号id
    private EnterPasswordDialog dialog;
    private PasswordInputView passwordInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechage);
        ButterKnife.bind(this);
        orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        initToolbar();
        name = getIntent().getStringExtra("name");
        idNum = getIntent().getStringExtra("idnum");
        phone = getIntent().getStringExtra("phone");
        platformUserNo = getIntent().getStringExtra("platformUserNo");

        idTvName.setText(TextUtils.isEmpty(name) ? "" : RegexUtil.hideFirstName(name));
        idTvBankCard.setText(TextUtils.isEmpty(idNum) ? "" : RegexUtil.hideBankCardId(idNum));
    }

    String POINT = ".";

    @OnTextChanged(value = R.id.id_ed_recharge_money, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void editeChanged(Editable editable) {
        //解决显示的问题
        String s = editable.toString();
        if (!TextUtils.isEmpty(s) && s.toString().contains(POINT)) {
            boolean mustBeChange = false;
            if (s.toString().startsWith(POINT)) {// .  |  .01 这个要重置et的值
                s = "0" + s;
                mustBeChange = true;
            } else if (!s.toString().endsWith(POINT) && s.toString().indexOf(POINT) + 2 < s.toString().length() - 1) { // 10.234 这个是重置et值
                s = s.substring(0, s.toString().indexOf(POINT) + 2 + 1);
                mustBeChange = true;
            }
            if (mustBeChange) {
                idEdRechargeMoney.setText(s);
                idEdRechargeMoney.setSelection(idEdRechargeMoney.getText().length());
            }
        }
        idBtnRecgarge.setEnabled(!TextUtils.isEmpty(s) && Double.parseDouble(s) > 0);
    }

    @OnClick(R.id.id_btn_recgarge)
    void onRechangeClick() {
        mPresenter.getPayWay(orderNo);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("账户充值")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("限额说明", true, R.color.color_popup_btn)
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
                intentCode.putExtra("type", RepaymentPresenter.TYPE_RECHARGE);//充值
                intentCode.putExtra("platformUserNo", platformUserNo);
                intentCode.putExtra("amount", Double.parseDouble(idEdRechargeMoney.getText().toString().trim()));
                startActivity(intentCode);
                break;
            case RepaymentPresenter.REPAYMENT_RECHARGE://密码支付成功
                Intent intent = new Intent(this, RechargeSuccessActivity.class);
                intent.putExtra("amount", Double.parseDouble(idEdRechargeMoney.getText().toString()));
                startActivity(intent);
                //通知我的账户刷新数据
                EventBusUtil.sendEvent(new Event(EventConfig.RECHARGE_OK));
                finish();
                break;
            case RepaymentPresenter.REPAYMENT_PAYWAY://支付方式
                OtherBean bean = (OtherBean) message.obj;
                if ("01".equals(bean.rechargeValidate)) {//短信验证码方式
                    mPresenter.getSmsVerifyCode(0, RepaymentPresenter.TYPE_RECHARGE,
                            platformUserNo, orderNo, idEdRechargeMoney.getText().toString());
                } else if ("02".equals(bean.rechargeValidate)) {//交易密码方式
                    dialog = new EnterPasswordDialog(this, new EnterPasswordDialog.EnterPassWordCallback() {
                        @Override
                        public void somethingDone(View view) {
                            switch (view.getId()) {
                                case R.id.id_btn_forgive_password:
                                    startActivity(new Intent(provideContext(), SettingActivity.class));
                                    dialog.dismiss();
                                    break;
                                case R.id.id_ed_password:
                                    //密码支付
                                    String pwd = ((PasswordInputView) view).getText().toString();
                                    Params params = new Params();
                                    params.put("amount", idEdRechargeMoney.getText().toString().trim());
                                    params.put("orderNo", orderNo);
                                    params.put("otherPlatformId", platformUserNo);
                                    params.put("pwd", pwd);
                                    mPresenter.pwdRecharge(params);
                                    break;
                            }
                        }
                    });

                    dialog.show();
                    passwordInputView = dialog.findViewById(R.id.id_ed_password);
                    KeyBoardUtils.showKeyBoard(passwordInputView, provideContext());
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

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                //充值成功 关闭页面
                case EventConfig.RECHARGE_OK:
                    finish();
                    break;
            }
        }
    }

}
