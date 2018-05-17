package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.LoanMoneyContact;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.utils.M;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/30.
 * description:
 */

public class LoanMoneyPresenter implements LoanMoneyContact.Presenter {
    private LoanMoneyContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int LOAN_MONEY = 0x110;

    public LoanMoneyPresenter(LoanMoneyContact.View mView) {
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
    public void loanMoney(double loanAmt, String loanUse, String orderNo, int periodNumber, String repaymentType) {
        Params params = new Params();
        params.put("loanAmt",loanAmt);
        params.put("loanUse",loanUse);
        params.put("orderNo",orderNo);
        params.put("periodNumber",periodNumber);
        params.put("repaymentType",repaymentType);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().loanMoney(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data,LOAN_MONEY));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
