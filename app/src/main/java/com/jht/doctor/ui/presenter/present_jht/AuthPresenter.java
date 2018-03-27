package com.jht.doctor.ui.presenter.present_jht;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.HttpConfig;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean_jht.BankBean;
import com.jht.doctor.ui.bean_jht.UploadImgBean;
import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.ui.contact.contact_jht.AuthContact;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * AuthPresenter
 * Create at 2018/3/24 下午7:37 by mayakun
 */
public class AuthPresenter implements AuthContact.Presenter {

    public static final int GETBANK_OK = 0x110;
    public static final int UPLOADIMF_OK = 0x111;
    private final AuthContact.View mView;
    private CompositeSubscription mSubscription;
    private LoadingDialog mDialog;

    @Inject
    public AuthPresenter(AuthContact.View view) {
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
    public void getBanks() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBank(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<BankBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<BankBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GETBANK_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void uploadImg(String path) {
        File file = new File(path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
        MultipartBody.Part part2 = MultipartBody.Part.createFormData("type", "1");
//        RequestBody uid= RequestBody.create(MediaType.parse("text/plain"), "4811420");

        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().uploadSingleFile(part, part2))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<UploadImgBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<UploadImgBean> response) {
                        mView.onSuccess(M.createMessage(response.data, UPLOADIMF_OK));


                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                    }
                });
        mSubscription.add(subscription);


    }


}
