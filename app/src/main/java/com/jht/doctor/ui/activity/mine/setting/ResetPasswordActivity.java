package com.jht.doctor.ui.activity.mine.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.TradePwdContact;
import com.jht.doctor.ui.presenter.TradePwdPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.LogUtil;
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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * mayakun 2017/11/16
 * 重置交易密码画面
 */
public class ResetPasswordActivity extends BaseAppCompatActivity implements TradePwdContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_pwd_title)
    TextView tvPwdTitle;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.cb_eye)
    CheckBox cbEye;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Inject
    TradePwdPresenter mPresenter;
    private String phone;
    private boolean isReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        //是否是重置
        isReset = getIntent().getBooleanExtra("isrest", false);
        phone = CustomerApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            tvPhone.setText("手机号 " + RegexUtil.hidePhone(phone));
        }
        initToolbar();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle(isReset ? "重置交易密码" : "设置交易密码")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();

        tvPwdTitle.setText(isReset ? "新密码" : "交易密码");
        btnNext.setText(isReset ? "下一步" : "确定");
    }

    @OnTextChanged(value = R.id.et_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterCodeChanged(Editable s) {
        setBtnNextStatus();
    }

    @OnTextChanged(value = R.id.et_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPwdChanged(Editable s) {
        setBtnNextStatus();
    }

    //按钮状态
    private void setBtnNextStatus() {
        btnNext.setEnabled(etCode.getText().toString().trim().length() >= 6 && etPassword.getText().toString().trim().length() >= 6);
    }

    @OnClick({R.id.tv_sendcode, R.id.cb_eye, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendcode:
                if (TextUtils.isEmpty(phone)) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("手机号异常，请返回后重试");
                } else {
                    mPresenter.sendVerifyCode(phone);
                }
                break;
            case R.id.cb_eye://密码是否可见
                etPassword.setTransformationMethod(cbEye.isChecked() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etPassword.setSelection(etPassword.getText().length());
                break;
            case R.id.btn_next:
                if (isReset) {
                    //重置
                    LogUtil.d("重置密码");
                    mPresenter.resetTradePwd(phone, etCode.getText().toString().trim(), etPassword.getText().toString().trim());
                } else {
                    //初次设置
                    LogUtil.d("设置密码");
                    mPresenter.setTradePwd(phone, etCode.getText().toString().trim(), etPassword.getText().toString().trim());
                }
                break;
        }
    }

    /**
     * 验证码倒计时时间
     */
    private int time = 60;//每次验证请求需要间隔60S
    private Subscription subscription;

    private void sendTimeNumber() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time) //设置循环次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return time - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setEnabled(false);//不可点击
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        tvSendcode.setText("重新发送");//数据发送完后设置为原来的文字
                        tvSendcode.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                        tvSendcode.setText(aLong + "s");
                    }
                });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(CustomerApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case TradePwdPresenter.TRADE_SEND_CODE://发送验证码
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("验证码发送成功，请注意查收");
                KeyBoardUtils.showKeyBoard(etCode,this);
                sendTimeNumber();
                break;
            case TradePwdPresenter.TRADE_REST_PWD://设置密码成功
            case TradePwdPresenter.TRADE_SET_PWD://重置密码成功
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(isReset ? "重置交易密码成功" : "设置交易密码成功");
                finish();
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
