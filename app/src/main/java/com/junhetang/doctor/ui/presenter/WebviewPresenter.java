package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.WebViewContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author: mayakun
 * @date: 2017/12/8
 */
public class WebviewPresenter implements WebViewContact.Presenter {

    public static final int GET_CREDIT_URL = 0x110;
    private WebViewContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public WebviewPresenter(WebViewContact.View view) {
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
    public void getCreditUrl(String orderNo) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getCreditUrl(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_CREDIT_URL));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
