package com.bakon.android.ui.presenter;

import com.bakon.android.application.MyApplication;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.data.eventbus.MSG;
import com.bakon.android.data.http.Params;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.ui.base.BaseObserver;
import com.bakon.android.ui.bean.BaseConfigBean;
import com.bakon.android.ui.bean.BasePageBean;
import com.bakon.android.ui.bean.CheckPaperBean;
import com.bakon.android.ui.bean.CommPaperBean;
import com.bakon.android.ui.bean.CommPaperInfoBean;
import com.bakon.android.ui.bean.DrugBean;
import com.bakon.android.ui.bean.JiuZhenHistoryBean;
import com.bakon.android.ui.bean.OnlinePaperBackBean;
import com.bakon.android.ui.bean.PaperInfoBean;
import com.bakon.android.ui.bean.PatientFamilyBean;
import com.bakon.android.ui.bean.SearchDrugBean;
import com.bakon.android.ui.bean.UploadImgBean;
import com.bakon.android.ui.contact.OpenPaperContact;
import com.bakon.android.utils.FileUtil;
import com.bakon.android.utils.LogUtil;
import com.bakon.android.widget.dialog.LoadingDialog;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * OpenPaperPresenter 开方
 * Create at 2018/4/23 下午7:24 by mayakun
 */

public class OpenPaperPresenter implements OpenPaperContact.Presenter {
    private OpenPaperContact.View mView;

    private CompositeDisposable mDisposable;

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
    public static final int CLASSICSPAPER_UP = 0x126;
    public static final int GET_PAPER_INFO_OK = 0x127;
    public static final int CHANGE_DRUG_OK = 0x128;
    public static final int CHECK_DRUG_TYPE = 0x129;
    public static final int GET_JZR_BY_PHONE = 0x130;
    public static final int GET_JZR_LIST = 0x131;
    public static final int GET_JZR_HISTORY_MEDICINAL = 0x132;

    public OpenPaperPresenter(OpenPaperContact.View mView) {
        this.mView = mView;
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

        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().uploadSingleFile(partType, partFile, partTime, partSign))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<UploadImgBean>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<UploadImgBean> response) {
                        mView.onSuccess(MSG.createMessage(response.data, UPLOADIMF_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onSuccess(MSG.createMessage(errorMsg, UPLOADIMF_ERROR));
                    }
                });

    }

    @Override
    public void openPaperCamera(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().photoExtractionNew(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<String> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.msg, OPENPAPER_CAMERA_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void openPaperOnline(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().lineExtraction(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<OnlinePaperBackBean>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<OnlinePaperBackBean> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse, OPENPAPER_ONLINE_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void searchSkillName(String name) {
        Params params = new Params();
        params.put("search", name);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().searchSkillName(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<BaseConfigBean.Skill>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<List<BaseConfigBean.Skill>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, SEARCH_SKILL_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void searchDrugName(int store_id, String name) {
        Params params = new Params();
        params.put("store_id", store_id);
        params.put("search", name);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().searchDrugName(params))
                .compose(mView.toLifecycle())
                //不需要dialog
                .subscribe(new BaseObserver<HttpResponse<List<SearchDrugBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<SearchDrugBean>> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, SEARCH_DRUG_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void searchDrugPaperById(int store_id, int id, int type) {
        //Type 2：常用处方 3：经典处方
        Params params = new Params();
        params.put("store_id", store_id);
        params.put("id", id);
        params.put("datatype", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOftenmedInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<CommPaperInfoBean>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<List<CommPaperInfoBean>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, GET_COMMPAPER_INFO_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void getOftenmedList(int page, int type, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("type", type);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOftenmedList2(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<CommPaperBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<CommPaperBean>> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, GET_COMMPAPER_LIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void addOftenmed(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().addOftenmed(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<String> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, ADD_COMMPAPER_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void delOftenmed(String ids) {
        Params params = new Params();
        params.put("id", ids);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().delOftenmed(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<String> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, DEL_COMMPAPER_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });


    }

    @Override
    public void addChatRecord(String d_accid, String p_accid, int type, int source) {
        Params params = new Params();
        params.put("d_accid", d_accid);
        params.put("p_accid", p_accid);
        params.put("type", type);//1:问诊单 2:随诊单 3:开方
        params.put("source", source);//0:首页 1：聊天
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().addChatRecord(params))
//                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<String>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, ADD_CHAT_RECORD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("addChatRecord onError:" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getCheckPaperList(int type) {
        Params params = new Params();
        params.put("type", type);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getCheckPaperList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<CheckPaperBean>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<List<CheckPaperBean>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, GET_CHECKPAPERLIST_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void checkPaper(int id, int status, String remark) {
        Params params = new Params();
        params.put("id", id);
        params.put("status", status);
        params.put("remark", remark);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().checkPape(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<String> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, CHECKPAPER_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void getPaperHistoryList(int page, int status, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("status", status);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPaperHistoryList(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<CheckPaperBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<CheckPaperBean>> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, GET_PAPER_HISTORYLIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getJiuZhenHistoryList(int page, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getJiuZhenHistoryList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<JiuZhenHistoryBean>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<BasePageBean<JiuZhenHistoryBean>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, GET_JIUZHEN_HISTORYLIST_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void getJZRList(int page, String searchStr) {
        Params params = new Params();
        params.put("page", page);
        params.put("search", searchStr);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getJZRList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<PatientFamilyBean.JiuzhenBean>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<BasePageBean<PatientFamilyBean.JiuzhenBean>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, GET_JZR_LIST));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void classicsPaperUp(int id) {
        Params params = new Params();
        params.put("id", id);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().classicsPaperUp(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<String> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, CLASSICSPAPER_UP));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });


    }

    @Override
    public void getPaperInfo(int id) {
        Params params = new Params();
        params.put("id", id);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPaperInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<PaperInfoBean>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<PaperInfoBean> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, GET_PAPER_INFO_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void changeDrugType(int storId, int type, String drugJson) {
        Params params = new Params();
        params.put("store_id", storId);
        params.put("type", type);//1：精品 2：普通
        params.put("param", drugJson);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().changeDrugType(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<DrugBean>>>(mDialog) {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onSuccess(HttpResponse<List<DrugBean>> httpResponse) {
                mView.onSuccess(MSG.createMessage(httpResponse.data, CHANGE_DRUG_OK));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mView.onError(errorCode, errorMsg);
            }
        });

    }

    @Override
    public void getJZRByPhone(String phone) {
        Params params = new Params();
        params.put("phone", phone);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPatientByPhone(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<List<PatientFamilyBean.JiuzhenBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<PatientFamilyBean.JiuzhenBean>> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, GET_JZR_BY_PHONE));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getJZRHistoryPaper(int pageNum, String phone, String name, String memb_no) {
        Params params = new Params();
        params.put("page", pageNum);
        params.put("phone", phone);
        params.put("name", name);
        params.put("memb_no", memb_no);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        MyApplication.getAppComponent().dataRepo().http()
                .wrapper(MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().getJzrHistoryMedicinal(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<PaperInfoBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<PaperInfoBean>> httpResponse) {
                        mView.onSuccess(MSG.createMessage(httpResponse.data, GET_JZR_HISTORY_MEDICINAL));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
    }


}
