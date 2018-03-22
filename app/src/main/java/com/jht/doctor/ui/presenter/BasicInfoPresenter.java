package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.contact.BasicInfoContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.ui.bean.ApplyInfoBean;

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
                        mView.onSuccess(M.createMessage(response.result, COMMIT_BASICINFO));
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
                        mView.onSuccess(M.createMessage(applyInfoBeanHttpResponse.result,APPLY_INFO));
                    }
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        mSubscription.add(subscription);

    }
}
