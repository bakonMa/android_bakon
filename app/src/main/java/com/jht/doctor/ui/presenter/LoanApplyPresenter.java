package com.jht.doctor.ui.presenter;

import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.ui.contact.LoanApplyContact;
import com.jht.doctor.utils.M;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author: mayakun
 * @date: 2017/12/8
 */
public class LoanApplyPresenter implements LoanApplyContact.Presenter {

    public static final int GET_CREDIT_STATUS = 0x111;
    private LoanApplyContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public LoanApplyPresenter(LoanApplyContact.View view) {
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
    public void getOrderCreditStatus(String orderNo) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOrderCreditStatus(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.result, GET_CREDIT_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
