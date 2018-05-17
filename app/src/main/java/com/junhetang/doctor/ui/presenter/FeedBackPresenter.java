package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.FeedBackContact;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.utils.M;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mayakun on 2017/12/15
 * description: 用户反馈
 */
public class FeedBackPresenter implements FeedBackContact.Presenter {

    private FeedBackContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    public static final int COMMIT_MESSAGE = 0x110;

    @Inject
    public FeedBackPresenter(FeedBackContact.View view) {
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
    public void confirmMessage(String content) {
        Params params = new Params();
        params.put("content", content);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().feedBack(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, COMMIT_MESSAGE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
