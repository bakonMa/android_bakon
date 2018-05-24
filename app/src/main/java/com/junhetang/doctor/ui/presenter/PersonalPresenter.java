package com.junhetang.doctor.ui.presenter;

import com.google.gson.Gson;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * PersonalPresenter 个人中心
 * Create at 2018/4/3 上午10:30 by mayakun
 */
public class PersonalPresenter implements PersonalContact.Presenter {
    private PersonalContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int GET_USEBASE_INFO = 0x110;
    public static final int ADD_USER_BASEINFO = 0x112;
    public static final int GET_VISITINFO_OK = 0x113;
    public static final int SET_VISITINFO_OK = 0x114;
    public static final int GET_AUTH_STATUS = 0x115;

    public PersonalPresenter(PersonalContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getUserIdentifyStatus() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserIdentifyStatus(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        U.setAuthStatus(resultResponse.data.status);
                        U.setAuthStatusFailMsg(resultResponse.data.fail_msg);
                        mView.onSuccess(M.createMessage(resultResponse.data, GET_AUTH_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getUserBasicInfo() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserBasicInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<UserBaseInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<UserBaseInfoBean> personalBeanHttpResponse) {
                        //sp 本地持久化
                        U.saveUserInfo(new Gson().toJson(personalBeanHttpResponse.data));
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_USEBASE_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        ToastUtil.showShort(errorCode);
                    }
                });
        compositeSubscription.add(subscription);
    }

    //个人公告和简介的提交
    @Override
    public void addUserbasic(String content, int type) {
        Params params = new Params();
        params.put("user_content", content);
        //提交类型标识 1：简介 2：公告
        params.put("post_type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().addUserbasic(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, ADD_USER_BASEINFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void setVisitInfo(String first, String again) {
        Params params = new Params();
        params.put("first_diagnose", first);
        params.put("second_diagnose", again);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().setVisitPrice(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, SET_VISITINFO_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }


}
