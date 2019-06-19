package com.bakon.android.ui.presenter;

import com.google.gson.Gson;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.data.http.Params;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.ui.base.BaseObserver;
import com.bakon.android.ui.bean.OtherBean;
import com.bakon.android.ui.bean.UserBaseInfoBean;
import com.bakon.android.ui.contact.PersonalContact;
import com.bakon.android.data.eventbus.MSG;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.utils.U;
import com.bakon.android.widget.dialog.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * PersonalPresenter 个人中心
 * Create at 2018/4/3 上午10:30 by mayakun
 */
public class PersonalPresenter implements PersonalContact.Presenter {
    private PersonalContact.View mView;

    private CompositeDisposable mDisposable;

    private LoadingDialog mDialog;

    public static final int GET_USEBASE_INFO = 0x110;
    public static final int ADD_USER_BASEINFO = 0x112;
    public static final int GET_VISITINFO_OK = 0x113;
    public static final int SET_VISITINFO_OK = 0x114;
    public static final int GET_AUTH_STATUS = 0x115;

    public PersonalPresenter(PersonalContact.View mView) {
        this.mView = mView;
        mDisposable = new CompositeDisposable();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (null != mDialog) {
            mDialog = null;
        }
        mView = null;
    }

    @Override
    public void getUserIdentifyStatus() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserIdentifyStatus(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        U.setAuthStatus(resultResponse.data.status);
                        U.setAuthStatusFailMsg(resultResponse.data.fail_msg);
                        mView.onSuccess(MSG.createMessage(resultResponse.data, GET_AUTH_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getUserBasicInfo() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserBasicInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<UserBaseInfoBean>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<UserBaseInfoBean> personalBeanHttpResponse) {
                //sp 本地持久化
                U.saveUserInfo(new Gson().toJson(personalBeanHttpResponse.data));
                mView.onSuccess(MSG.createMessage(personalBeanHttpResponse.data, GET_USEBASE_INFO));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                ToastUtil.showShort(errorCode);
            }
        });

    }

    //个人公告和简介的提交
    @Override
    public void addUserbasic(String content, int type) {
        Params params = new Params();
        params.put("user_content", content);
        //提交类型标识 1：简介 2：公告
        params.put("post_type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().addUserbasic(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> resultResponse) {
                        mView.onSuccess(MSG.createMessage(resultResponse.data, ADD_USER_BASEINFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void setVisitInfo(String first) {
        Params params = new Params();
        params.put("first_diagnose", first);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().setVisitPrice(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> resultResponse) {
                        mView.onSuccess(MSG.createMessage(resultResponse.data, SET_VISITINFO_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }


}
