package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.HomeLoanBean;
import com.jht.doctor.ui.bean.MaxAmtBean;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public class HomeLoanPresenter implements HomeLoanContact.Presenter {
    private HomeLoanContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int APPLY_SUCCESS = 0x110;

    public static final int REPAYMENT = 0x111;

    public static final int MAX_AMT = 0x112;

    public HomeLoanPresenter(HomeLoanContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if(!compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void applyStatus() {
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().applyStatus())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<HomeLoanBean>>(mDialog) {
            @Override
            public void onSuccess(HttpResponse<HomeLoanBean> homeLoanBeanHttpResponse) {
                mView.onSuccess(M.createMessage(homeLoanBeanHttpResponse.result,APPLY_SUCCESS));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode,errorMsg);
            }
        });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getMaxAmt() {
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getMAxAmt())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<MaxAmtBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MaxAmtBean> maxAmtBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(maxAmtBeanHttpResponse.result,MAX_AMT));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
