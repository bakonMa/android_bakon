package com.junhetang.doctor.ui.presenter;

import com.google.gson.Gson;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.AuthInfoBean;
import com.junhetang.doctor.ui.bean.BaseConfigBean;
import com.junhetang.doctor.ui.bean.HospitalBean;
import com.junhetang.doctor.ui.bean.UploadImgBean;
import com.junhetang.doctor.ui.contact.AuthContact;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    private AuthContact.View mView;
    private CompositeDisposable mDisposable;
    private LoadingDialog mDialog;

    @Inject
    public AuthPresenter(AuthContact.View view) {
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
    public void getHospital(String prov, String city) {
        Params params = new Params();
        params.put("prov", prov);
        params.put("city", city);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHospital(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<List<HospitalBean>>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<HospitalBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GETHOSPITAL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        ToastUtil.show(errorMsg);
                    }
                });
    }

    @Override
    public void uploadImg(String path, String type) {
        //type：0：头像 1：其他认证图片  upload：图片文件
        MultipartBody.Part partType = MultipartBody.Part.createFormData("type", type);
        File file = new File(path);
        byte[] bytes = FileUtil.zipImageToSize(file, FileUtil.MAX_UPLOAD_SIZE_1M);
        LogUtil.d("bytes after size=" + bytes.length);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bytes);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);

        Params params = new Params();
        params.put("type", type);
        MultipartBody.Part partTime = MultipartBody.Part.createFormData(HttpConfig.TIMESTAMP, params.get(HttpConfig.TIMESTAMP).toString());
        MultipartBody.Part partSign = MultipartBody.Part.createFormData(HttpConfig.SIGN_KEY, params.getSign(params));

        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().uploadSingleFile(partType, partFile, partTime, partSign))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<UploadImgBean>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<UploadImgBean> response) {
                        mView.onSuccess(M.createMessage(response.data, UPLOADIMF_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onSuccess(M.createMessage(errorMsg, UPLOADIMF_ERROR));
                    }
                });
    }

    //获取科室、职称、擅长等信息
    @Override
    public void getDpAndTitles() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getDpAndTitles(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<BaseConfigBean>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

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
    }

    /**
     * 认证1
     */
    @Override
    public void userIdentify(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userIdentify(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        //修改本地认证状态
                        U.setAuthStatus(1);
                        mView.onSuccess(M.createMessage(response.data, USER_IDENTIFY_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
    }

    @Override
    public void userIdentifyNext(String idCard, String path1, String path2, String path3) {
        Params params = new Params();
        params.put("idcard", idCard);
        params.put("idcard_img", path1);//身份证id
        params.put("qa_img", path2);//资格证地址
        params.put("pro_img", path3);//执业证地址
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userIdentifyNext(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, USER_CREDENTIAL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
    }

    @Override
    public void getUserIdentify() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getUserIdentify(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<AuthInfoBean>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<AuthInfoBean> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_AUTHINFO_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorMsg, errorMsg);
                    }
                });
    }
}
