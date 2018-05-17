package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.SupportBankContact;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.ui.bean.SupportBankBean;
import com.junhetang.doctor.utils.M;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/12.
 * description:
 */

public class SupportBankPresenter implements SupportBankContact.Presenter {
    private SupportBankContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int SUPPORT_BANKLIST = 0x110;


    public SupportBankPresenter(SupportBankContact.View mView) {
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
    public void getSupportankList(String orderNo) {
        Params params = new Params();
        params.put("orderNo",orderNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().supportBankList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<List<SupportBankBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<SupportBankBean>> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data,SUPPORT_BANKLIST));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
