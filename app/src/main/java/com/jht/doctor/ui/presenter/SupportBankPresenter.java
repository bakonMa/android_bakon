package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.ui.bean.SupportBankBean;
import com.jht.doctor.ui.contact.SupportBankContact;
import com.jht.doctor.utils.M;

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
