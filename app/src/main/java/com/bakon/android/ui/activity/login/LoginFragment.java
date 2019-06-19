package com.bakon.android.ui.activity.login;

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

import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.http.DataRepository;
import com.bakon.android.data.localdata.MMKVManager;
import com.bakon.android.di.components.DaggerFragmentComponent;
import com.bakon.android.di.modules.FragmentModule;
import com.bakon.android.ui.activity.fragment.MainActivity;
import com.bakon.android.ui.activity.print.PrintActivity;
import com.bakon.android.ui.base.BaseFragment;
import com.bakon.android.ui.contact.LoginContact;
import com.bakon.android.ui.presenter.LoginPresenter;
import com.bakon.android.utils.LogUtil;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.utils.U;
import com.bakon.android.utils.UmengKey;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
    @BindView(R.id.tv_frogetpwd)
    TextView tvFrogetpwd;
    @BindView(R.id.tv_registe)
    TextView tvRegiste;

    @Inject
    LoginPresenter mPresenter;

    @Inject
    DataRepository dataRepository;

    @Inject
    MMKVManager mmkvManager;

    /**
     * 验证码倒计时时间
     */
    private int time = 60;//每次验证请求需要间隔60S
    private Disposable disposable;
    private int type = 0;//0：验证码 1：密码

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
        tvFrogetpwd.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        //上次登录的手机号
        etPhone.setText(U.getPhone());
        etPhone.setSelection(etPhone.getText().length());

        String path = dataRepository.appSP().getString(SPConfig.SP_STR_PHONE);
        LogUtil.d("di", path);
        mmkvManager.mmkv.putString("key1", "v1");
        LogUtil.d("di", "key1=" + mmkvManager.mmkv.getString("key1", ""));
        LogUtil.d("di", "key=" + mmkvManager.mmkv.getString("key", "11111111"));

    }

    @OnClick({R.id.tv_registe, R.id.tv_frogetpwd, R.id.btn_login, R.id.tv_sendcode,
            R.id.iv_code_clean, R.id.iv_phone_clean, R.id.iv_pwd_clean,
            R.id.iv_pwd_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_frogetpwd://忘记密码
                Intent intent = new Intent(actContext(), ResetPasswordActivity.class);
                intent.putExtra("title", "忘记密码");
                startActivity(intent);
                break;
            case R.id.tv_registe://注册
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), UmengKey.login_registe);
                startActivity(new Intent(actContext(), RegisteActivity.class));
                break;
            case R.id.btn_login://登录
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), type == 0 ? UmengKey.login_code : UmengKey.login_password);

//                mPresenter.login(etPhone.getText().toString().trim(),
//                        type == 0 ? etCode.getText().toString().trim() : etPassword.getText().toString().trim(),
//                        type);

                startActivity(new Intent(getActivity(), PrintActivity.class));


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
                .applicationComponent(MyApplication.getAppComponent())
                .build()
                .inject(this);
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
        ToastUtil.show(errorMsg);
    }

    //倒计时
    public void timeDown() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time) //设置循环次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return time - aLong;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tvSendcode.setEnabled(false);//不可点击
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {//接受到一条就是会操作一次UI
                        tvSendcode.setText(aLong + "s后可重发");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tvSendcode.setText("重新获取验证码");//数据发送完后设置为原来的文字
                        tvSendcode.setEnabled(true);
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
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        mPresenter.unsubscribe();
    }
}
