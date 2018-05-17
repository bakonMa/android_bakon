package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.ApplyInfoBean;
import com.junhetang.doctor.ui.contact.JobInfoContact;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.utils.M;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by table on 2017/11/27.
 * description:
 */

public class JobInfoPresenter implements JobInfoContact.Presenter {
    private JobInfoContact.View mView;
    private CompositeSubscription compositeSubscription;
    private LoadingDialog mDialog;
    public static final int COMMIT_JOB_INFO = 0x110;
    public static final int APPLY_INFO = 0x112;

    public JobInfoPresenter(JobInfoContact.View mView) {
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
    public void commitJobInfo(String companyName,String companyType,String industry,String position,double monthlyIncome) {
        Params params = new Params();
        params.put("companyName",companyName);
        params.put("companyType",companyType);
        params.put("industry",industry);
        params.put("position",position);
        params.put("monthlyIncome",monthlyIncome);
        //params.put("userId",1);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().commitJobInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{if(mDialog!=null) mDialog.show();})
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data,COMMIT_JOB_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void requestInfo() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getApplyInfo())
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<ApplyInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<ApplyInfoBean> applyInfoBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(applyInfoBeanHttpResponse.data,APPLY_INFO));
                    }
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
