package com.renxin.doctor.activity.ui.presenter;

import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.RepaymentHomeBean;
import com.renxin.doctor.activity.ui.contact.HomeRepaymentContact;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

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
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHomeRepayment())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<RepaymentHomeBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<RepaymentHomeBean> repaymentHomeBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(repaymentHomeBeanHttpResponse.data, REPAYMENT));
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
//                        mView.onSuccess(M.createMessage(httpResponse.data,MESSAGE_COUNT));
//                    }
//
//                    @Override
//                    public void onError(String code, String msg) {
//                        mView.onError(code,msg);
//                    }
//                });
//        compositeSubscription.add(subscription);
//    }
}
