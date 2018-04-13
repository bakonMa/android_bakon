package com.renxin.doctor.activity.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.ui.activity.home.MainActivity;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.contact.LoginContact;
import com.renxin.doctor.activity.ui.presenter.LoginPresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

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
 * mayakun 2017/11/15
 * 登录画面
 */
public class LoginFragment extends BaseFragment implements LoginContact.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_phone_clean)
    ImageView ivPhoneClean;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_code_clean)
    ImageView ivCodeClean;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.llt_code)
    LinearLayout lltCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_pwd_clean)
    ImageView ivPwdClean;
    @BindView(R.id.llt_password)
    LinearLayout lltPassword;
    @BindView(R.id.iv_pwd_eye)
    ImageView ivPwdEye;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_registe)
    TextView tvRegiste;

    @Inject
    LoginPresenter mPresenter;

    /**
     * 验证码倒计时时间
     */
    private int time = 60;//每次验证请求需要间隔60S
    private Subscription subscription;
    private int type = 0;

    //根据type，构造fragment
    public static LoginFragment newInstance(int type) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        type = getArguments().getInt("type", 0);
        lltCode.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        lltPassword.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.tv_registe, R.id.btn_login, R.id.tv_sendcode,
            R.id.iv_code_clean, R.id.iv_phone_clean, R.id.iv_pwd_clean,
            R.id.iv_pwd_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_registe://注册
                startActivity(new Intent(actContext(), RegisteActivity.class));
                break;
            case R.id.btn_login://登录
                mPresenter.login(etPhone.getText().toString().trim(),
                        type == 0 ? etCode.getText().toString().trim() : etPassword.getText().toString().trim(),
                        type);
                break;
            case R.id.iv_phone_clean://手机清除
                etPhone.setText("");
                break;
            case R.id.iv_code_clean://验证码清除
                etCode.setText("");
                break;
            case R.id.iv_pwd_clean://密码清除
                etPhone.setText("");
                break;
            case R.id.iv_pwd_eye://密码可视状态
                ivPwdEye.setSelected(!ivPwdEye.isSelected());
                etPassword.setTransformationMethod(ivPwdEye.isSelected() ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etPassword.setSelection(etPassword.getText().length());
                break;
            case R.id.tv_sendcode://发送验证码
                mPresenter.sendMsgCode(etPhone.getText().toString().trim(), 1);
                break;
        }
    }

    //手机号监听
    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        ivPhoneClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
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

    //按钮状态
    private void setBtnNextStatus() {
        if (type == 0) {//验证码
            tvSendcode.setEnabled(etPhone.getText().toString().trim().length() == 11);
            btnLogin.setEnabled(etCode.getText().toString().trim().length() >= 6
                    && etPhone.getText().toString().trim().length() == 11);
        } else { //密码
            btnLogin.setEnabled(etPassword.getText().toString().trim().length() >= 6
                    && etPhone.getText().toString().trim().length() == 11);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case LoginPresenter.LOGIN_SUCCESS://登录成功
                startActivity(new Intent(actContext(), MainActivity.class));
                getActivity().finish();
                break;
            case LoginPresenter.SEND_CODE://发送验证码成功
                timeDown();
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorCode);
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
    public Activity provideContext() {
        return getActivity();
    }


    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消订阅
        if (subscription != null) {
            subscription.unsubscribe();
        }
        mPresenter.unsubscribe();
    }
}
