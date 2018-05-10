package com.renxin.doctor.activity.ui.presenter;

import com.google.gson.Gson;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.BannerBean;
import com.renxin.doctor.activity.ui.bean.OPenPaperBaseBean;
import com.renxin.doctor.activity.ui.bean.OtherBean;
import com.renxin.doctor.activity.ui.contact.WorkRoomContact;
import com.renxin.doctor.activity.utils.LogUtil;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * OpenPaperPresenter 开方
 * Create at 2018/4/23 下午7:24 by mayakun
 */

public class WorkRoomPresenter implements WorkRoomContact.Presenter {
    private WorkRoomContact.View mView;

    private CompositeSubscription mSubscription;

    private LoadingDialog mDialog;

    public static final int GET_BASEDATA_0K = 0x110;
    public static final int UPLOADIMF_OK = 0x111;
    public static final int GET_AUTH_STATUS = 0x112;
    public static final int GET_BANNER_OK = 0x113;

    public WorkRoomPresenter(WorkRoomContact.View mView) {
        this.mView = mView;
        mSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
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
                        mView.onSuccess(M.createMessage(resultResponse.data, GET_AUTH_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getRedPointStatus() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHomeRedPointStatus(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        //审方
                        U.setRedPointExt(resultResponse.data.ext_status);
                        //系统消息
                        U.setRedPointSys(resultResponse.data.sys_status);
                        //患者添加
                        U.setRedPointFir(resultResponse.data.fri_status);
                        //TODO 红点状态红点是否显示
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME));
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME_CHECK));
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME_SYSMSG));
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_PATIENT));

//                        mView.onSuccess(M.createMessage(resultResponse.data, GET_AUTH_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("getRedPointStatus errorMsg:" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getHomeBanner() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHomeBanner(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<BannerBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<BannerBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_BANNER_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    //开方基础数据
    @Override
    public void getOPenPaperBaseData() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getSomeadvisory(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OPenPaperBaseBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<OPenPaperBaseBean> personalBeanHttpResponse) {
                        U.saveOpenpaperBaseData(new Gson().toJson(personalBeanHttpResponse.data));
//                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_BASEDATA_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    //更新token
    @Override
    public void updataToken() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().updateToken(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<OtherBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> response) {
                        U.updateToken(response.data.token);
                        LogUtil.d("updataToken success:");
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
//                        mView.onError(errorCode, errorMsg);
                        LogUtil.d("updataToken error:" + errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
