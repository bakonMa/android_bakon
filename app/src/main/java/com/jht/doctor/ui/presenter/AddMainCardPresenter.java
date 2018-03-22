package com.jht.doctor.ui.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.BankBean;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.contact.AddMainCardContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class AddMainCardPresenter implements AddMainCardContact.Presenter {
    private AddMainCardContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int GET_BANK_NAME = 0x110;
    public static final int TRADE_PWD_STATUS = 0x111;
    public static final int APPLY_ATHORATION_ONLINE = 0x112;
    public static final int APPLY_ATHORATION_OFFLINE = 0x113;
    public static final int SETPASSWORD = 0x114;

    public AddMainCardPresenter(AddMainCardContact.View mView) {
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
    public void getBankName(String cardNo) {
        Params params = new Params();
        params.put("cardBin", cardNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBankName(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BankBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<BankBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, GET_BANK_NAME));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        //mView.onError(errorCode,errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }

    //判断是否有交易密码(设置页面判断时使用，这里不适用，已废弃)
    @Override
    public void tradePwdStatus() {
        Params params = new Params();
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().tradePwdStatus(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.result, TRADE_PWD_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void bindCardTradePwdStatus(String orderNo, String otherPlatformId) {
        Params params = new Params();
        params.put("orderNo",orderNo);
        params.put("otherPlatformId",otherPlatformId);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().bindCardTradePwdStatus(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(()->{
                    if(mDialog!=null){
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<OtherBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.result, TRADE_PWD_STATUS));
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
                        String result = httpResponse.result.toString().replaceAll("=,","=null,");
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
                            Log.e("error", e.getMessage());
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
                        mView.onSuccess(M.createMessage(httpResponse.result,SETPASSWORD));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }


}
