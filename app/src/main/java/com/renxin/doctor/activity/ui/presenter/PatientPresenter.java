package com.renxin.doctor.activity.ui.presenter;

import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.MaxAmtBean;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.bean.HomeLoanBean;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * PatientPresenter 患者
 * Create at 2018/4/14 下午11:41 by mayakun 
 */

public class PatientPresenter implements PatientContact.Presenter {
    private PatientContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int APPLY_SUCCESS = 0x110;

    public static final int REPAYMENT = 0x111;

    public static final int MAX_AMT = 0x112;

    public PatientPresenter(PatientContact.View mView) {
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
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().applyStatus())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<HomeLoanBean>>(mDialog) {
            @Override
            public void onSuccess(HttpResponse<HomeLoanBean> homeLoanBeanHttpResponse) {
                mView.onSuccess(M.createMessage(homeLoanBeanHttpResponse.data,APPLY_SUCCESS));
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
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getMAxAmt())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<MaxAmtBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MaxAmtBean> maxAmtBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(maxAmtBeanHttpResponse.data,MAX_AMT));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
