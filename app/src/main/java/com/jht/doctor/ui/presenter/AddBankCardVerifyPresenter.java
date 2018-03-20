package com.jht.doctor.ui.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.ApplyUserBean;
import com.jht.doctor.ui.bean.UserAuthorizationBO;
import com.jht.doctor.ui.contact.AddBankCardVerifyContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class AddBankCardVerifyPresenter implements AddBankCardVerifyContact.Presenter {
    private AddBankCardVerifyContact.View mView;

    private LoadingDialog mDialog;

    private CompositeSubscription compositeSubscription;

    public static final int APPLY_USER = 0x111;

    public static final int APPLY_ATHORATION_ONLINE = 0x112;

    public AddBankCardVerifyPresenter(AddBankCardVerifyContact.View mView) {
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
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().applyAuthorization(params))
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
                            ApplyAuthorizationBean applyAuthorizationBean = new Gson().fromJson(result, ApplyAuthorizationBean.class);
                            if ("200".equals(applyAuthorizationBean.getLainlianDTO().getCode())) {
                                //线上成功
                                mView.onSuccess(M.createMessage(applyAuthorizationBean, APPLY_ATHORATION_ONLINE));
                            } else {
                                //线上失败、连连返回
                                mView.onError(applyAuthorizationBean.getLainlianDTO().getCode(), applyAuthorizationBean.getLainlianDTO().getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("tag", result);
                            mView.onError("", result);
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
    public void userAuthorization(ApplyAuthorizationBean.ApplyAuthorizationDTOBean applyAuthorizationDTOBean, UserAuthorizationBO userAuthorizationBO) {
        Params params = new Params();
        params.put("applyAuthorizationBO", applyAuthorizationDTOBean);
        params.put("userAuthorizationBO", userAuthorizationBO);
        Subscription subscription = CustomerApplication.getAppComponent().dataRepo().http()
                .wrapper(CustomerApplication.getAppComponent().dataRepo().http().provideHttpAPI().userAuthorization(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<ApplyUserBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<ApplyUserBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result,APPLY_USER));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }

                });
        compositeSubscription.add(subscription);
    }
}
