package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.contact.MyLoanContact;
import com.junhetang.doctor.ui.bean.MyLoanBean;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public class MyLoanPresenter implements MyLoanContact.Presenter {
    private MyLoanContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int MY_LOAN_LIST = 0x110;

    public MyLoanPresenter(MyLoanContact.View mView) {
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
    public void getLoanList() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getMyLoan())
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<List<MyLoanBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<MyLoanBean>> listHttpResponse) {
                        mView.onSuccess(M.createMessage(listHttpResponse.data,MY_LOAN_LIST));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
