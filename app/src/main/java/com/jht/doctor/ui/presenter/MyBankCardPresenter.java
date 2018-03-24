package com.jht.doctor.ui.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.ContributiveBean;
import com.jht.doctor.ui.bean.IfBankOfJointBean;
import com.jht.doctor.ui.bean.JudgeIfTiedBean;
import com.jht.doctor.ui.contact.MyBankCardContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.ui.bean.BankCardBean;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/11.
 * description:
 */

public class MyBankCardPresenter implements MyBankCardContact.Presenter {
    private MyBankCardContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int GET_BANK_LIST = 0x110;
    public static final int JUDGE_IS_TIED_MAIN = 0x111;//判断主借人是否绑过卡
    public static final int JUDGE_IS_TIED_JOINT = 0x112;//判断是否有历史共借人
    public static final int UNBIND = 0x113;
    public static final int ENSURE_CARD = 0x114;
    public static final int IF_BANK_JOINT = 0x115;
    public static final int CONTRIBUTIVE = 0x116;
    public static final int ENSURE_CONTRIBUTION = 0x117;
    public static final int SETPASSWORD = 0x118;
    public static final int APPLY_ATHORATION_ONLINE = 0x119;
    public static final int APPLY_ATHORATION_OFFLINE = 0x120;
    public static final int CONTRIBUTIVE_JOINT = 0x121;


    public MyBankCardPresenter(MyBankCardContact.View mView) {
        this.mView = mView;
        mDialog = new LoadingDialog(mView.provideContext());
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if(!compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getBankList(String orderNo) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBankList(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<BankCardBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<BankCardBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, GET_BANK_LIST));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void contributiveTypeJudge(String idCardNo, String mobile, String orderNo, String realName,String type) {
        Params params = new Params();
        params.put("idCardNo", idCardNo);
        params.put("mobile", mobile);
        params.put("orderNo", orderNo);
        params.put("realName", realName);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().contributiveTypeJudge(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<ContributiveBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<ContributiveBean> httpResponse) {
                        if("0".equals(type)){
                            mView.onSuccess(M.createMessage(httpResponse.data, CONTRIBUTIVE));
                        }else if("1".equals(type)){
                            mView.onSuccess(M.createMessage(httpResponse.data, CONTRIBUTIVE_JOINT));
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void ensureBankCardOfCunGuan(String bankCard, String bankMobile, String bankName, String idCard,
                                        String orderNo, String otherPlatformId, String userName, String userType) {
        Params params = new Params();
        params.put("bankCard", bankCard);
        params.put("bankMobile", bankMobile);
        params.put("bankName", bankName);
        params.put("idCard", idCard);
        params.put("orderNo", orderNo);
        params.put("otherPlatformId", otherPlatformId);
        params.put("userName", userName);
        params.put("userType", userType);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().ensureBankCardOfCunGuan(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ENSURE_CONTRIBUTION));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void judgeIsTiedCard(String orderNo, String userType) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        params.put("userType", userType);//0 为主借人 1 共借人
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().ifTileBank(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<JudgeIfTiedBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<JudgeIfTiedBean> httpResponse) {
                        if ("0".equals(userType)) {
                            mView.onSuccess(M.createMessage(httpResponse.data, JUDGE_IS_TIED_MAIN));
                        } else {
                            mView.onSuccess(M.createMessage(httpResponse.data, JUDGE_IS_TIED_JOINT));
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void unbind(String bankCardNo,String orderNo) {
        Params params = new Params();
        params.put("bankCardNo", bankCardNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().deleteBankCard(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, UNBIND));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void ensureCard(String bankCardNo, String orderNo) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        params.put("bankCardNo", bankCardNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().ensureBankCard(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ENSURE_CARD));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void ifBankJoint(String idCareNo, String orderNo, String userName) {
        Params params = new Params();
        params.put("idCareNo", idCareNo);
        params.put("orderNo", orderNo);
        params.put("userName", userName);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().ifBankOfJoint(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<IfBankOfJointBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<IfBankOfJointBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, IF_BANK_JOINT));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void bindCardSetTradePwd(String orderNo, String otherPlatformId, String pwd) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        params.put("otherPlatformId", otherPlatformId);
        params.put("pwd", pwd);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().bindCardSetTradePwd(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data,SETPASSWORD));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void addMainCard(String orderNo, String bankCardNo, String userName,
                            String bankPhone, String userType, String bankCardName,
                            String idCard, String password) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        params.put("bankCardNo", bankCardNo);
        params.put("userName", userName);
        params.put("bankPhone", bankPhone);
        params.put("userType", userType);
        params.put("bankCardName", bankCardName);
        params.put("idCard", idCard);
        params.put("password", password);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().applyAuthorization(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse httpResponse) {
                        String result = httpResponse.data.toString().replaceAll("=,","=null,");
                        try {
                            Gson gson = new Gson();
                            ApplyAuthorizationBean applyAuthorizationBean = gson.fromJson(result, new TypeToken<ApplyAuthorizationBean>() {}.getType());
                            if ("200".equals(applyAuthorizationBean.getLainlianDTO().getCode())) {
                                //线上成功
                                mView.onSuccess(M.createMessage(applyAuthorizationBean, APPLY_ATHORATION_ONLINE));
                            } else {
                                //线上失败、连连返回
                                mView.onError(applyAuthorizationBean.getLainlianDTO().getCode(), applyAuthorizationBean.getLainlianDTO().getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("tag", e.getMessage());
                            if ("00000000".equals(result)) {
                                //线下成功
                                mView.onSuccess(M.createMessage(result, APPLY_ATHORATION_OFFLINE));
                            } else {
                                //线上、线下失败
                                mView.onError("", result);
                            }
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
