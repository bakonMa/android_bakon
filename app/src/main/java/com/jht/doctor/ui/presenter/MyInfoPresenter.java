package com.jht.doctor.ui.presenter;

import com.jht.doctor.ui.contact.MyInfoContact;
import com.jht.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

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
//        Params params = new Params();
//        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
//                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserInfo(params))
//                .compose(mView.toLifecycle())
//                .doOnSubscribe(() -> {
//                    if (mDialog != null) mDialog.show();
//                })
//                .subscribe(new BaseObserver<HttpResponse<MyInfoBean>>(mDialog) {
//                    @Override
//                    public void onSuccess(HttpResponse<MyInfoBean> loginResponseHttpResponse) {
//                        Message message = Message.obtain();
//                        message.obj = loginResponseHttpResponse.data;
//                        mView.onSuccess(message);
//                    }
//
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        mView.onError(errorCode, errorMsg);
////                        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(msg);
//                    }
//                });
//        mSubscription.add(subscription);
    }

}
