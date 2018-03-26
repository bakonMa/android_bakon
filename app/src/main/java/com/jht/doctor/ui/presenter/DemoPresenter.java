package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.LoginResponse;
import com.jht.doctor.ui.contact.DemoContact;
import com.jht.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/22.
 * description:
 */
public class DemoPresenter implements DemoContact.Presenter {

    private final DemoContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public DemoPresenter(DemoContact.View view) {
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
    public void login() {
        Params params = new Params();
        params.put("mobilePhone", "13276386385");
        params.put("checkCode", "123456");
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().login(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<LoginResponse>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<LoginResponse> loginResponseHttpResponse) {

                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }
}
