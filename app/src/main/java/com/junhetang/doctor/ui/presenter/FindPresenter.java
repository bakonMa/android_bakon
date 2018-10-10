package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.NewsInfoBean;
import com.junhetang.doctor.ui.contact.FindContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * WalletPresenter 钱包
 * Create at 2018/4/10 下午2:49 by mayakun
 */
public class FindPresenter implements FindContact.Presenter {

    public static final int GET_NEWS_OK = 0x110;

    private FindContact.View mView;
    private CompositeDisposable mDisposable;
    private LoadingDialog mDialog;

    @Inject
    public FindPresenter(FindContact.View view) {
        this.mView = view;
        mDisposable = new CompositeDisposable();
        mDialog = new LoadingDialog(mView.provideContext());
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

    @Override
    public void getNewsList(int type, int page) {
        Params params = new Params();
        params.put("type", type);
        params.put("page", page);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getNewslist(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<NewsInfoBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<NewsInfoBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_NEWS_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
    }
}
