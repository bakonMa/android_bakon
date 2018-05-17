package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.ui.contact.TradePwdContact;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.utils.M;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mayakun on 2017/11/29
 * 交易密码
 */

public class TradePwdPresenter implements TradePwdContact.Presenter {
    private final TradePwdContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mdialog;
    public static final int TRADE_SEND_CODE = 0x110;
    public static final int TRADE_PWD_STATUS = 0x111;
    public static final int TRADE_SET_PWD = 0x112;
    public static final int TRADE_REST_PWD = 0x113;

    @Inject
    public TradePwdPresenter(TradePwdContact.View view) {
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

    //判断是否有交易密码
    @Override
    public void tradePwdStatus() {
        Params params = new Params();
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().tradePwdStatus(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, TRADE_PWD_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }


    @Override
    public void sendVerifyCode(String phone) {
        Params params = new Params();
        params.put("mobilePhone", phone);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().sendTradeCode(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, TRADE_SEND_CODE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }


    //初次设置交易密码
    @Override
    public void setTradePwd(String mobilePhone, String code, String pwd) {
        Params params = new Params();
        params.put("code", code);
        params.put("mobilePhone", mobilePhone);
        params.put("pwd", pwd);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().setTradePwd(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, TRADE_SET_PWD));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    //重置交易密码
    @Override
    public void resetTradePwd(String mobilePhone, String code, String pwd) {
        Params params = new Params();
        params.put("mobilePhone", mobilePhone);
        params.put("code", code);
        params.put("pwd", pwd);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().resetTradePwd(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, TRADE_REST_PWD));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
