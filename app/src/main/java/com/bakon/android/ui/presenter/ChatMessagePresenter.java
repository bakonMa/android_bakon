package com.bakon.android.ui.presenter;

import com.bakon.android.application.MyApplication;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.data.eventbus.MSG;
import com.bakon.android.data.http.Params;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.ui.base.BaseObserver;
import com.bakon.android.ui.bean.CommMessageBean;
import com.bakon.android.ui.contact.ChatMessageContact;
import com.bakon.android.widget.dialog.LoadingDialog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * ChatMessagePresenter 聊天相关
 * Create at 2018/4/19 下午12:02 by mayakun
 */
public class ChatMessagePresenter implements ChatMessageContact.Presenter {
    private ChatMessageContact.View mView;
    private CompositeDisposable mDisposable;
    private LoadingDialog mDialog;

    public static final int GET_USERFUL_OK = 0x110;//获取常用语
    public static final int ADD_USERFUL_OK = 0x111;//添加常用语
    public static final int DEL_USERFUL_OK = 0x112;//删除常用语

    @Inject
    public ChatMessagePresenter(ChatMessageContact.View view) {
        this.mView = view;
        mDialog = new LoadingDialog(mView.provideContext());
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (null != mDialog) {
            mDialog = null;
        }
        mView = null;
    }

    //用户获取常用语
    @Override
    public void getuseful(int type) {
        Params params = new Params();
        params.put("type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getuseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<CommMessageBean>>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<CommMessageBean>> stringHttpResponse) {
                        mView.onSuccess(MSG.createMessage(stringHttpResponse.data, GET_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void adduseful(int type, String message) {
        Params params = new Params();
        params.put("type", type);
        params.put("content", message);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().adduseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(MSG.createMessage(stringHttpResponse.data, ADD_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void deluseful(String ids) {
        Params params = new Params();
        params.put("id", ids);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().deluseful(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> stringHttpResponse) {
                        mView.onSuccess(MSG.createMessage(stringHttpResponse.data, DEL_USERFUL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }


}
