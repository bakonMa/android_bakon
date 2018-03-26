package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.MessageCountBean;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.ui.bean.MyAccountInfoBean;
import com.jht.doctor.ui.bean.PersonalBean;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public class PersonalPresenter implements PersonalContact.Presenter {
    private PersonalContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int GET_PERSONAL_INFO = 0x110;
    public static final int GET_USER_INFO = 0x111;
    public static final int ACCOUNT_INFO = 0x112;
    public static final int MESSAGE_COUNT = 0x113;

    public PersonalPresenter(PersonalContact.View mView) {
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
    public void getPersonalInfo() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPersonalInfo())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<PersonalBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<PersonalBean> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_PERSONAL_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getUserInfo() {
        Params params = new Params();
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<MyInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MyInfoBean> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, GET_USER_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getAccount() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().selectUserAccountInfo())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<MyAccountInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MyAccountInfoBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ACCOUNT_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getMessageCount() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().selectCount())
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<MessageCountBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MessageCountBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data,MESSAGE_COUNT));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode,errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
