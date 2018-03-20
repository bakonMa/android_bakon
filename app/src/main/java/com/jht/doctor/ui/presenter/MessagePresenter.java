package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.contact.MessageContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.ui.bean.MessageBean;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author: mayakun
 * @date: 2017/11/27
 * @project: customer-android-2th
 * @detail:
 */
public class MessagePresenter implements MessageContact.Presenter {

    private MessageContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;
    public static final int MESSAGE_LIST = 0x110;
    public static final int MESSAGE_DELETE = 0x111;

    @Inject
    public MessagePresenter(MessageContact.View view) {
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
    public void getMessageList(int pageNo) {
        Params params = new Params();
        params.put("pageNo", pageNo);
        params.put("pageSize", U.PAGE_SIZE);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().getMessageList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null && pageNo == 1) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<MessageBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<MessageBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, MESSAGE_LIST));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void deleteMessage(int id) {
        Params params = new Params();
        params.put("id", id);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().deleteMessage(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, MESSAGE_DELETE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);

    }
}
