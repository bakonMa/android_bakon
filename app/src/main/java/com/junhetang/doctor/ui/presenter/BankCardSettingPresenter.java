package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.BankCardSettingContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.ui.bean.BankCardBean;

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
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBankList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<BankCardBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BankCardBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data,GET_BANK_LIST));
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
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().deleteBankCard(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data,UNBIND));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
