package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.nim.NimU;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CheckPaperBean;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.ui.bean.PatientBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * PatientPresenter 患者
 * Create at 2018/4/14 下午11:41 by mayakun
 */

public class PatientPresenter implements PatientContact.Presenter {
    private PatientContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int GET_PATIENTLIST_0K = 0x110;
    public static final int GET_PATIENTFAMILY_0K = 0x111;
    public static final int SET_PRICE_0K = 0x112;
    public static final int TOTALK_OK = 0x113;
    public static final int ADD_PATIENT_JZR_OK = 0x114;
    public static final int GET_PATIEN_PAPER_TLIST_0K = 0x115;
    public static final int SEARCH_PATIENT_OK = 0x116;
    public static final int ADD_PATIENT_OK = 0x117;

    public PatientPresenter(PatientContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }


    @Override
    public void getPatientlist() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getpatientlist(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<List<PatientBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<List<PatientBean>> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_PATIENTLIST_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void searchPatient(String search) {
        Params params = new Params();
        params.put("search", search);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getpatientlist(params))
                .compose(mView.toLifecycle())
//                .doOnSubscribe(() -> {
//                    if (mDialog != null)
//                        mDialog.show();
//                })
                .subscribe(new BaseObserver<HttpResponse<List<PatientBean>>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<List<PatientBean>> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, SEARCH_PATIENT_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getpatientFamily(String memb_no) {
        Params params = new Params();
        params.put("memb_no", memb_no);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getpatientinfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<PatientFamilyBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<PatientFamilyBean> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_PATIENTFAMILY_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);

    }

    @Override
    public void setRemarkName(String accID, String memb_no, String remarkName) {
        Params params = new Params();
        params.put("p_accid", accID);
        params.put("memb_no", memb_no);
        params.put("remark_name", remarkName);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().setRemarkName(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, GET_PATIENTFAMILY_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void setPrice(String memb_no, String advisory_fee) {
        Params params = new Params();
        params.put("memb_no", memb_no);
        params.put("advisory_fee", advisory_fee);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().setAdvisoryfee(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, SET_PRICE_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void docToTalk(String accid) {
        Params params = new Params();
        params.put("daccid", NimU.getNimAccount());
        params.put("saccid", accid);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().docToTalk(params))
//                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<String>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        LogUtil.d("docToTalk ok");
                        mView.onSuccess(M.createMessage(httpResponse.data, TOTALK_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("docToTalk error=" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addPatient(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().addPatient(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ADD_PATIENT_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addPatientJZR(Params params) {
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().addPatientJZR(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<String>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ADD_PATIENT_JZR_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getPatientPaper(int page, String patient_id, String memb_no) {
        Params params = new Params();
        params.put("page", page);
        params.put("patient_id", patient_id);
        params.put("memb_no", memb_no);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getPatientPaperlist(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<BasePageBean<CheckPaperBean>>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<CheckPaperBean>> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, GET_PATIEN_PAPER_TLIST_0K));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
