package com.junhetang.doctor.ui.presenter;

import com.google.gson.Gson;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.BannerBean;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.ui.bean.SystemMsgBean;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

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
    public static final int GET_SYSTEMMSG_LIST_OK = 0x114;

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
                        U.setAuthStatusFailMsg(resultResponse.data.fail_msg);
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

                        //审核处方 红点
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME_CHECK));
                        //患者 红点
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_PATIENT));
                        //【工作室】红点,logo红点
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME));
                        //系统消息 红点
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME_SYSMSG));
                        //不需要前台处理
                        //mView.onSuccess(M.createMessage(resultResponse.data, GET_AUTH_STATUS));
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

    //后台绑定信鸽token
    @Override
    public void bindXGToken(String xgToken) {
        Params params = new Params();
        params.put("c_token", xgToken);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().bindXGToken(params))
                .subscribe(new BaseObserver<HttpResponse<String>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        LogUtil.d("bingXGToken OK");
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("bingXGToken onError" + errorMsg);
                    }

                });
        mSubscription.add(subscription);
    }

    @Override
    public void getSystemMsgList(int page) {
        Params params = new Params();
        params.put("page", page);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getSystemMsglist(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<SystemMsgBean>>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<SystemMsgBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_SYSTEMMSG_LIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
