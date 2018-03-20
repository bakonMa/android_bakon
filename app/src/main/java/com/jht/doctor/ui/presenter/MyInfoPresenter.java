package com.jht.doctor.ui.presenter;

import android.os.Message;

import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.contact.MyInfoContact;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author: mayakun
 * @date: 2017/11/27
 * @project: customer-android-2th
 * @detail:
 */
public class MyInfoPresenter implements MyInfoContact.Presenter {

    private MyInfoContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public MyInfoPresenter(MyInfoContact.View view) {
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
    public void getMyInfo() {
        Params params = new Params();
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<MyInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MyInfoBean> loginResponseHttpResponse) {
                        Message message = Message.obtain();
                        message.obj = loginResponseHttpResponse.result;
                        mView.onSuccess(message);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
//                        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
