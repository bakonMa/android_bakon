package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.BaseConfigBean;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CheckPaperBean;
import com.junhetang.doctor.ui.bean.CommPaperBean;
import com.junhetang.doctor.ui.bean.CommPaperInfoBean;
import com.junhetang.doctor.ui.bean.JiuZhenHistoryBean;
import com.junhetang.doctor.ui.bean.OnlinePaperBackBean;
import com.junhetang.doctor.ui.bean.SearchDrugBean;
import com.junhetang.doctor.ui.bean.UploadImgBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

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

    public static final int UPLOADIMF_OK = 0x111;
    public static final int UPLOADIMF_ERROR = 0x112;
    public static final int OPENPAPER_CAMERA_OK = 0x113;
    public static final int SEARCH_SKILL_OK = 0x114;
    public static final int SEARCH_DRUG_OK = 0x115;
    public static final int GET_COMMPAPER_INFO_OK = 0x116;
    public static final int GET_COMMPAPER_LIST_OK = 0x117;
    public static final int ADD_COMMPAPER_OK = 0x118;
    public static final int DEL_COMMPAPER_OK = 0x119;
    public static final int OPENPAPER_ONLINE_OK = 0x120;
    public static final int ADD_CHAT_RECORD_OK = 0x121;
    public static final int GET_CHECKPAPERLIST_OK = 0x122;
    public static final int CHECKPAPER_OK = 0x123;
    public static final int GET_PAPER_HISTORYLIST_OK = 0x124;
    public static final int GET_JIUZHEN_HISTORYLIST_OK = 0x125;

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
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().photoExtractionNew(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, OPENPAPER_CAMERA_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void openPaperOnline(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().lineExtraction(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<OnlinePaperBackBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OnlinePaperBackBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, OPENPAPER_ONLINE_OK));
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
    public void searchDrugName(int store_id, String name) {
        Params params = new Params();
        params.put("store_id", store_id);
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
    public void searchDrugPaperById(int store_id, int id, int type) {
        //Type 2：常用处方 3：经典处方
        Params params = new Params();
        params.put("store_id", store_id);
        params.put("id", id);
        params.put("datatype", type);
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
    public void getOftenmedList(int page, int type, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("type", type);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOftenmedList2(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<CommPaperBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<CommPaperBean>> personalBeanHttpResponse) {
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

    @Override
    public void delOftenmed(String ids) {
        Params params = new Params();
        params.put("id", ids);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().delOftenmed(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, DEL_COMMPAPER_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);

    }

    @Override
    public void addChatRecord(String d_accid, String p_accid, int type, int source) {
        Params params = new Params();
        params.put("d_accid", d_accid);
        params.put("p_accid", p_accid);
        params.put("type", type);//1:问诊单 2:随诊单 3:开方
        params.put("source", source);//0:首页 1：聊天
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().addChatRecord(params))
//                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<String>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, ADD_CHAT_RECORD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("addChatRecord onError:" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getCheckPaperList(int type) {
        Params params = new Params();
        params.put("type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getCheckPaperList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<CheckPaperBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<CheckPaperBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_CHECKPAPERLIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void checkPaper(int id, int status, String remark) {
        Params params = new Params();
        params.put("id", id);
        params.put("status", status);
        params.put("remark", remark);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().checkPape(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, CHECKPAPER_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getPaperHistoryList(int page, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPaperHistoryList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<CheckPaperBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<CheckPaperBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_PAPER_HISTORYLIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getJiuZhenHistoryList(int page, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getJiuZhenHistoryList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<JiuZhenHistoryBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<JiuZhenHistoryBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_JIUZHEN_HISTORYLIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

}
