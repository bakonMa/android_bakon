package com.renxin.doctor.activity.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.H5Config;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.activity.WebViewActivity;
import com.renxin.doctor.activity.ui.activity.fragment.MainActivity;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.contact.LoginContact;
import com.renxin.doctor.activity.ui.presenter.LoginPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * RegisteActivity  注册
 * Create at 2018/4/2 下午3:41 by mayakun
 */
public class RegisteActivity extends BaseActivity implements LoginContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_phone_clean)
    ImageView ivPhoneClean;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_code_clean)
    ImageView ivCodeClean;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_pwd_clean)
    ImageView ivPwdClean;
    @BindView(R.id.et_sec_password)
    EditText etSecPassword;
    @BindView(R.id.iv_sec_pwd_clean)
    ImageView ivSecPwdClean;
    @BindView(R.id.btn_registe)
    Button btnRegiste;
    @BindView(R.id.iv_pwd_eye)
    ImageView ivPwdEye;
    @BindView(R.id.iv_sec_pwd_eye)
    ImageView ivSecPwdEye;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;

    @Inject
    LoginPresenter mPresenter;

    /**
     * 验证码倒计时时间
     */
    private int time = 60;//每次验证请求需要间隔60S
    private Subscription subscription;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_registe;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("快速注册")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                })
                .bind();

    }

    @OnClick({R.id.btn_registe, R.id.tv_sendcode,
            R.id.iv_code_clean, R.id.iv_phone_clean, R.id.iv_pwd_clean, R.id.iv_sec_pwd_clean,
            R.id.iv_pwd_eye, R.id.iv_sec_pwd_eye, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_registe://注册
                if (!etPassword.getText().toString().trim().equals(etSecPassword.getText().toString().trim())) {
                    showDialog("两次密码不一致");
                    return;
                } else if (!cbAgreement.isChecked()) {
                    showDialog("未同意用户协议");
                    return;
                }

                mPresenter.regist(etPhone.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etCode.getText().toString().trim());
                break;
            case R.id.tv_sendcode://发送验证码
                mPresenter.sendMsgCode(etPhone.getText().toString().trim(), 0);
                break;
            case R.id.iv_phone_clean://手机清除
                etPhone.setText("");
                break;
            case R.id.iv_code_clean://验证码清除
                etCode.setText("");
                break;
            case R.id.iv_pwd_clean://密码清除
                etPassword.setText("");
                break;
            case R.id.iv_sec_pwd_clean://密码清除
                etSecPassword.setText("");
            case R.id.iv_pwd_eye://密码可视状态
                ivPwdEye.setSelected(!ivPwdEye.isSelected());
                etPassword.setTransformationMethod(ivPwdEye.isSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etPassword.setSelection(etPassword.getText().length());
                break;
            case R.id.iv_sec_pwd_eye://密码可视状态
                ivSecPwdEye.setSelected(!ivSecPwdEye.isSelected());
                etSecPassword.setTransformationMethod(ivSecPwdEye.isSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etSecPassword.setSelection(etSecPassword.getText().length());
                break;
            case R.id.tv_agreement://用户协议
                WebViewActivity.startAct(actContext(),
                        true,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_AGREEMENT,
                        H5Config.H5_AGREEMENT_TITLE,
                        H5Config.H5_AGREEMENT);
                break;
        }
    }

    //手机号监听
    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        ivPhoneClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        tvSendcode.setEnabled(s.length() == 11);//验证码是否可以点击
        setBtnNextStatus();
    }

    //验证码监听
    @OnTextChanged(value = R.id.et_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterCodeChanged(Editable s) {
        ivCodeClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        setBtnNextStatus();
    }

    //密码监听
    @OnTextChanged(value = R.id.et_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPwdChanged(Editable s) {
        ivPwdClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        setBtnNextStatus();
    }

    //密码监听
    @OnTextChanged(value = R.id.et_sec_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSecPwdChanged(Editable s) {
        ivSecPwdClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        setBtnNextStatus();
    }

    //按钮状态
    private void setBtnNextStatus() {
        btnRegiste.setEnabled(
                etPhone.getText().toString().trim().length() == 11
                        && etCode.getText().toString().trim().length() == 6
                        && etPassword.getText().toString().trim().length() >= 6
                        && etSecPassword.getText().toString().trim().length() >= 6);
    }

    //展示确定dialog
    private CommonDialog commonDialog;

    private void showDialog(String title) {
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }

        commonDialog = new CommonDialog(this, title);
        commonDialog.show();
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        showDialog(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case LoginPresenter.SEND_CODE://注册 发送短信成功
                timeDown();
                break;
            case LoginPresenter.REGISTE_SUCCESS://注册成功
                ToastUtil.show("注册成功，已帮您自动登录");
                //自动登录
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    //倒计时
    public void timeDown() {
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
                        tvSendcode.setText("重新获取验证码");//数据发送完后设置为原来的文字
                        tvSendcode.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                        tvSendcode.setText(aLong + "s后可重发");
                    }
                });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
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
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (subscription != null) {
            subscription.unsubscribe();
        }
        mPresenter.unsubscribe();
    }
}
