package com.jht.doctor.ui.presenter.present_jht;

import com.google.gson.Gson;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.HttpConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean_jht.AuthInfoBean;
import com.jht.doctor.ui.bean_jht.BaseConfigBean;
import com.jht.doctor.ui.bean_jht.HospitalBean;
import com.jht.doctor.ui.bean_jht.UploadImgBean;
import com.jht.doctor.ui.contact.AuthContact;
import com.jht.doctor.utils.FileUtil;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.M;
import com.jht.doctor.utils.ToastUtil;
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

    public static final int GETHOSPITAL_OK = 0x110;
    public static final int UPLOADIMF_OK = 0x111;
    public static final int UPLOADIMF_ERROR = 0x112;
    public static final int GET_BASECONFIG = 0x113;
    public static final int USER_IDENTIFY_OK = 0x114;
    public static final int USER_CREDENTIAL_OK = 0x115;
    public static final int GET_AUTHINFO_OK = 0x116;

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
    public void getHospital(String prov, String city) {
        Params params = new Params();
        params.put("prov", prov);
        params.put("city", city);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHospital(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<HospitalBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<HospitalBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GETHOSPITAL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void uploadImg(String path, String type) {
        //type：0：头像 1：其他认证图片  upload：图片文件
        MultipartBody.Part partType = MultipartBody.Part.createFormData("type", type);
        File file = new File(path);
        LogUtil.d("bytes befor size=" + file.length());
        byte[] bytes = FileUtil.zipImageToSize(file, FileUtil.MAX_UPLOAD_SIZE);
        LogUtil.d("bytes after size=" + bytes.length);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bytes);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);

        Params params = new Params();
        params.put("type", "1");
        MultipartBody.Part partTime = MultipartBody.Part.createFormData(HttpConfig.TIMESTAMP, params.get(HttpConfig.TIMESTAMP).toString());
        MultipartBody.Part partSign = MultipartBody.Part.createFormData(HttpConfig.SIGN_KEY, params.getSign(params));

        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().uploadSingleFile(partType, partFile, partTime, partSign))
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
                        mView.onSuccess(M.createMessage(errorMsg, UPLOADIMF_ERROR));
                    }
                });
        mSubscription.add(subscription);
    }

    //获取科室、职称、擅长等信息
    @Override
    public void getDpAndTitles() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getDpAndTitles(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<BaseConfigBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BaseConfigBean> response) {
                        //本地存储使用
                        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_KEY_BASE_CONFIG, new Gson().toJson(response));

                        mView.onSuccess(M.createMessage(response.data, GET_BASECONFIG));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    /**
     * 认证1
     */
    @Override
    public void userIdentify(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userIdentify(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, USER_IDENTIFY_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void userIdentifyNext(String idCard, String path1, String path2, String path3) {
        Params params = new Params();
        params.put("idcard", idCard);
        params.put("idcard_img", path1);//身份证id
        params.put("qa_img", path2);//资格证地址
        params.put("pro_img", path3);//执业证地址
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userIdentifyNext(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, USER_CREDENTIAL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getUserIdentify() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserIdentify(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<AuthInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<AuthInfoBean> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_AUTHINFO_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }


}
