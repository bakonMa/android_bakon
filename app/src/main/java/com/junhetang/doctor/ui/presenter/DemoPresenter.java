package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.LoginResponse;
import com.junhetang.doctor.ui.contact.DemoContact;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by table on 2017/11/22.
 * description:
 */
public class DemoPresenter implements DemoContact.Presenter {

    private DemoContact.View mView;
    private CompositeDisposable mDisposable;
    private LoadingDialog mDialog;

    @Inject
    public DemoPresenter(DemoContact.View view) {
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
    public void login() {
        Params params = new Params();
        params.put("mobilePhone", "13276386385");
        params.put("checkCode", "123456");
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().login(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<LoginResponse>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<LoginResponse> loginResponseHttpResponse) {

                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                });
    }
}
