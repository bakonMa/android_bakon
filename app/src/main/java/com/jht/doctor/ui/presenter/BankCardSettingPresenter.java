package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.contact.BankCardSettingContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.ui.bean.BankCardBean;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class BankCardSettingPresenter implements BankCardSettingContact.Presenter {
    private BankCardSettingContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int GET_BANK_LIST = 0x110;
    public static final int UNBIND = 0x111;

    public BankCardSettingPresenter(BankCardSettingContact.View mView) {
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
    public void getBankList(String orderNo) {
        Params params = new Params();
        params.put("orderNo",orderNo);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBankList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<BankCardBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BankCardBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result,GET_BANK_LIST));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void unbind(String bankCardNo,String orderNo) {
        Params params = new Params();
        params.put("bankCardNo",bankCardNo);
        params.put("orderNo",orderNo);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().deleteBankCard(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result,UNBIND));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
