package com.renxin.doctor.activity.ui.presenter;

import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.contact.SupportBankContact;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;
import com.renxin.doctor.activity.ui.bean.SupportBankBean;
import com.renxin.doctor.activity.utils.M;

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
