package com.renxin.doctor.activity.ui.presenter;

import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.OPenPaperBaseBean;
import com.renxin.doctor.activity.ui.bean_jht.BaseConfigBean;
import com.renxin.doctor.activity.ui.bean_jht.CommPaperBean;
import com.renxin.doctor.activity.ui.bean_jht.CommPaperInfoBean;
import com.renxin.doctor.activity.ui.bean_jht.SearchDrugBean;
import com.renxin.doctor.activity.ui.bean_jht.UploadImgBean;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.utils.FileUtil;
import com.renxin.doctor.activity.utils.LogUtil;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * OpenPaperPresenter 开方
 * Create at 2018/4/23 下午7:24 by mayakun
 */

public class OpenPaperPresenter implements OpenPaperContact.Presenter {
    private OpenPaperContact.View mView;

    private CompositeSubscription mSubscription;

    private LoadingDialog mDialog;

    public static final int GET_BASEDATA_0K = 0x110;
    public static final int UPLOADIMF_OK = 0x111;
    public static final int UPLOADIMF_ERROR = 0x112;
    public static final int OPENPAPER_OK = 0x113;
    public static final int SEARCH_SKILL_OK = 0x114;
    public static final int SEARCH_DRUG_OK = 0x115;
    public static final int GET_COMMPAPER_INFO_OK = 0x116;
    public static final int GET_COMMPAPER_LIST_OK = 0x117;
    public static final int ADD_COMMPAPER_OK = 0x118;

    public OpenPaperPresenter(OpenPaperContact.View mView) {
        this.mView = mView;
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
    public void getOPenPaperBaseData() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getSomeadvisory(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<OPenPaperBaseBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OPenPaperBaseBean> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_BASEDATA_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
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
        params.put("type", type);
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

    @Override
    public void openPaperCamera(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().photoExtraction(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, OPENPAPER_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void searchSkillName(String name) {
        Params params = new Params();
        params.put("search", name);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().searchSkillName(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<BaseConfigBean.Skill>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<BaseConfigBean.Skill>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, SEARCH_SKILL_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void searchDrugName(String name) {
        Params params = new Params();
        params.put("search", name);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().searchDrugName(params))
                .compose(mView.toLifecycle())
                //不需要dialog
                .subscribe(new BaseObserver<HttpResponse<List<SearchDrugBean>>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<List<SearchDrugBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, SEARCH_DRUG_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void searchDrugPaperById(int id) {
        Params params = new Params();
        params.put("id", id);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOftenmedInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<CommPaperInfoBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<CommPaperInfoBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_COMMPAPER_INFO_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getOftenmedList() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOftenmedList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<CommPaperBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<CommPaperBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_COMMPAPER_LIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void addOftenmed(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().addOftenmed(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, ADD_COMMPAPER_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
