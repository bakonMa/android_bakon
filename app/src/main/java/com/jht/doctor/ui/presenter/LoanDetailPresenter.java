package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.contact.LoanDetailContact;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.ui.bean.LoanDetailBean;
import com.jht.doctor.utils.M;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/7.
 * description:
 */

public class LoanDetailPresenter implements LoanDetailContact.Presenter {
    private LoanDetailContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int LOAN_DETAIL = 0x110;

    public LoanDetailPresenter(LoanDetailContact.View mView) {
        this.mView = mView;
        mDialog = new LoadingDialog(mView.provideContext());
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if(!compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getLoanDetail(String orderNO) {
        Params params = new Params();
        params.put("orderNo", orderNO);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getLoanDetail(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<LoanDetailBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<LoanDetailBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, LOAN_DETAIL));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }
}
