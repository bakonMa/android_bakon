package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.ApplyInfoBean;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.ui.contact.BasicInfoContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/27.
 * description: 用户基本信息
 */
public class BasicInfoPresenter implements BasicInfoContact.Presenter {

    private final BasicInfoContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    public static final int COMMIT_BASICINFO = 0x110;
    public static final int APPLY_INFO = 0x111;

    @Inject
    public BasicInfoPresenter(BasicInfoContact.View view) {
        this.mView = view;
        mSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void commitInfo(String userName, String certNo) {
        Params params = new Params();
        params.put("userName", userName);
        params.put("certNo", certNo);
        //params.put("id", 1);
        params.put("certType", "01");
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().commitBasicInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, COMMIT_BASICINFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
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
        mSubscription.add(subscription);

    }
}
