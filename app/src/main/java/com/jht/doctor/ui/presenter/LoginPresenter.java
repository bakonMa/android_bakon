package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.LoginResponse;
import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.utils.M;

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
    public static final int SENDVERIFY_CODE = 0x110;

    public static final int LOGIN_SUCCESS = 0x111;

    public static final int BIND = 0x112;

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

    @Override
    public void sendVerifyCode(String phone) {
        Params params = new Params();
        params.put("phone", phone);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().sendVerifyCode(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, SENDVERIFY_CODE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void login(String phone, String code) {
        Params params = new Params();
        params.put("mobilePhone", phone);
        params.put("checkCode", code);
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
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, loginResponseHttpResponse.data.getToken());
                        mView.onSuccess(M.createMessage(loginResponseHttpResponse.data, LOGIN_SUCCESS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void bind(String phone) {
        Params params = new Params();
        params.put("invitationNo", phone);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userBindSale(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) {
                        mdialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, BIND));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.bindError(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
