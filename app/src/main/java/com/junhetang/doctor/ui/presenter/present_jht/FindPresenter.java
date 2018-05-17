package com.junhetang.doctor.ui.presenter.present_jht;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.base.BasePageBean;
import com.junhetang.doctor.ui.bean.NewsInfoBean;
import com.junhetang.doctor.ui.contact.FindContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * WalletPresenter 钱包
 * Create at 2018/4/10 下午2:49 by mayakun
 */
public class FindPresenter implements FindContact.Presenter {

    public static final int GET_NEWS_OK = 0x110;

    private final FindContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public FindPresenter(FindContact.View view) {
        this.mView = view;
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
    public void getNewsList(int type, int page) {
        Params params = new Params();
        params.put("type", type);
        params.put("page", page);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getNewslist(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<NewsInfoBean>>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<NewsInfoBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_NEWS_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
