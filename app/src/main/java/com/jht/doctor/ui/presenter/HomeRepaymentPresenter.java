package com.jht.doctor.ui.presenter;

import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.ui.bean.RepaymentHomeBean;
import com.jht.doctor.ui.contact.HomeRepaymentContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public class HomeRepaymentPresenter implements HomeRepaymentContact.Presenter {
    private HomeRepaymentContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int REPAYMENT = 0x111;
    public static final int MESSAGE_COUNT = 0x112;

    public HomeRepaymentPresenter(HomeRepaymentContact.View mView) {
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
    public void getHomeRepayment() {
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHomeRepayment())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<RepaymentHomeBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<RepaymentHomeBean> repaymentHomeBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(repaymentHomeBeanHttpResponse.result, REPAYMENT));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);

    }

//    @Override
//    public void getMessageCount() {
//        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
//                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().selectCount())
//                .compose(mView.toLifecycle())
//                .doOnSubscribe(()->{
//                    if(mDialog!=null){
//                        mDialog.show();
//                    }
//                }).subscribe(new BaseObserver<HttpResponse<MessageCountBean>>(mDialog) {
//                    @Override
//                    public void onSuccess(HttpResponse<MessageCountBean> httpResponse) {
//                        mView.onSuccess(M.createMessage(httpResponse.result,MESSAGE_COUNT));
//                    }
//
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        mView.onError(errorCode,errorMsg);
//                    }
//                });
//        compositeSubscription.add(subscription);
//    }
}
