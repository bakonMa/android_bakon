package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.CommMessageBean;
import com.junhetang.doctor.ui.contact.ChatMessageContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ChatMessagePresenter 聊天相关
 * Create at 2018/4/19 下午12:02 by mayakun
 */
public class ChatMessagePresenter implements ChatMessageContact.Presenter {
    private final ChatMessageContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mdialog;

    public static final int GET_USERFUL_OK = 0x110;//获取常用语
    public static final int ADD_USERFUL_OK = 0x111;//添加常用语
    public static final int DEL_USERFUL_OK = 0x112;//删除常用语

    @Inject
    public ChatMessagePresenter(ChatMessageContact.View view) {
        this.mView = view;
        mdialog = new LoadingDialog(mView.provideContext());
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    //用户获取常用语
    @Override
    public void getuseful(int type) {
        Params params = new Params();
        params.put("type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getuseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<CommMessageBean>>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<CommMessageBean>> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, GET_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void adduseful(int type, String message) {
        Params params = new Params();
        params.put("type", type);
        params.put("content", message);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().adduseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, ADD_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void deluseful(String ids) {
        Params params = new Params();
        params.put("id", ids);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().deluseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(M.createMessage(stringHttpResponse.data, DEL_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }


}
