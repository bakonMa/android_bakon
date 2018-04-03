package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.HttpConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.LoginResponse;
import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/24.
 * description:
 */

public class LoginPresenter implements LoginContact.Presenter {
    private final LoginContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mdialog;

    public static final int SEND_CODE = 0x110;//发送验证码
    public static final int LOGIN_SUCCESS = 0x111;//登录

    @Inject
    public LoginPresenter(LoginContact.View view) {
        this.mView = view;
        mdialog = new LoadingDialog(mView.provideContext());
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    //登录获取验证码
    @Override
    public void sendMsgCode(String phone, int type) {
        //type: 0 注册 1登录 2忘记密码
        Params params = new Params();
        params.put("mobile", phone);
        params.put("type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().sendCode(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, SEND_CODE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void login(String phone, String code, int type) {
        Params params = new Params();
        params.put("mobile", phone);
        params.put("type", type);
        if (type == 0) {//验证码
            params.put("vcode", code);
        } else {//密码
            params.put("password", code);
        }
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));

        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().login(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<LoginResponse>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<LoginResponse> loginResponseHttpResponse) {
                        //保存token
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_PHONE, phone);
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, loginResponseHttpResponse.data.token);
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCID, loginResponseHttpResponse.data.accid);
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCTOKEN, loginResponseHttpResponse.data.acctoken);

                        mView.onSuccess(M.createMessage(loginResponseHttpResponse.data, LOGIN_SUCCESS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
