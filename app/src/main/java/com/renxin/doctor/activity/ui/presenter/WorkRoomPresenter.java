package com.renxin.doctor.activity.ui.presenter;

import com.google.gson.Gson;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.OPenPaperBaseBean;
import com.renxin.doctor.activity.ui.contact.WorkRoomContact;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

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
    public void getHomeData() {
//        Params params = new Params();
//        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
//        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
//                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getSomeadvisory(params))
//                .compose(mView.toLifecycle())
//                .doOnSubscribe(() -> {
//                    if (mDialog != null)
//                        mDialog.show();
//                }).subscribe(new BaseObserver<HttpResponse<OPenPaperBaseBean>>(mDialog) {
//                    @Override
//                    public void onSuccess(HttpResponse<OPenPaperBaseBean> personalBeanHttpResponse) {
//
//                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_BASEDATA_0K));
//                    }
//
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        mView.onError(errorCode, errorMsg);
//                    }
//                });
//        mSubscription.add(subscription);
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

}
