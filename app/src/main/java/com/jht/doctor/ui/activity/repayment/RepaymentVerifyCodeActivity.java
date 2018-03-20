package com.jht.doctor.ui.activity.repayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
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
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * mayakun
 * 充值&提现 验证码 确认
 */
public class RepaymentVerifyCodeActivity extends BaseAppCompatActivity implements RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.btn_comfirm)
    Button btnComfirm;

    @Inject
    RepaymentPresenter mPresenter;
    private String phone, orderNo, platformUserNo;
    private int type;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        ButterKnife.bind(this);
        initToolbar();

        phone = getIntent().getStringExtra("phone");
        orderNo = getIntent().getStringExtra("orderNo");

        //*必须传*
        if (!getIntent().hasExtra("type")) {
            CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("数据异常，请稍后重试");
            finish();
        } else {
            type = getIntent().getIntExtra("type", RepaymentPresenter.TYPE_RECHARGE);//0：充值 1：提现
        }
        platformUserNo = getIntent().getStringExtra("platformUserNo");
        amount = getIntent().getDoubleExtra("amount", 0d);

        tvPhone.setText(String.format("验证码已发送至 %s", RegexUtil.hidePhone(phone)));
        sendVerifyCode();//倒计时开始
    }

    @OnTextChanged(value = R.id.et_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterCodeChanged(Editable s) {
        btnComfirm.setEnabled(s.length() == 6);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("验证码")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick({R.id.tv_sendcode, R.id.btn_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendcode:
                mPresenter.getSmsVerifyCode(1, type, platformUserNo, orderNo, String.valueOf(amount));
                break;
            case R.id.btn_comfirm:
                Params params = new Params();
                params.put("amount", amount);
                params.put("orderNo", orderNo);
                params.put("otherPlatformId", platformUserNo);
                params.put("verifyCode", etCode.getText().toString().trim());
                switch (type) {
                    case RepaymentPresenter.TYPE_RECHARGE://充值
                        mPresenter.smsRecharge(params);
                        break;
                    case RepaymentPresenter.TYPE_WITHDRAW://提现
                        mPresenter.smsWithDraw(params);
                        break;

                }
                break;
        }
    }

    private int time = 60;
    private Subscription mSucsription;

    private void sendVerifyCode() {
        mSucsription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(60)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return time - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setEnabled(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tvSendcode.setText(aLong + "s");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setText("获取验证码");
                        tvSendcode.setEnabled(true);
                    }
                });
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
    protected void onDestroy() {
        super.onDestroy();
        if (mSucsription != null) {
            mSucsription.unsubscribe();
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case RepaymentPresenter.REPAYMENT_SMS_WITHDRAW://提现成功
                Intent intentWithDraw = new Intent(this, WithdrawSuccessActivity.class);
                intentWithDraw.putExtra("amount", amount);
                startActivity(intentWithDraw);
                //通知我的账户刷新数据 && 关闭前画面
                EventBusUtil.sendEvent(new Event(EventConfig.WITHDRAW_OK));
                finish();
                break;
            case RepaymentPresenter.REPAYMENT_SMS_RECHARGE://充值成功
                Intent intentRecharge = new Intent(this, RechargeSuccessActivity.class);
                intentRecharge.putExtra("amount", amount);
                startActivity(intentRecharge);
                //通知我的账户刷新数据 && 关闭前画面
                EventBusUtil.sendEvent(new Event(EventConfig.RECHARGE_OK));
                finish();
                break;
            case RepaymentPresenter.REPAYMENT_SMS_CODE_AGAIN://重新请求（发送验证码）
                sendVerifyCode();
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
}
