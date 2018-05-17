package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.contact.AddCoborrowerContact;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.ui.bean.ContributiveBean;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.ui.bean.AddCoborrowerBean;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/14.
 * description:
 */

public class AddCoborrowerPresenter implements AddCoborrowerContact.Presenter {
    private AddCoborrowerContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int ADD_COBORROWER = 0x110;
    public static final int CONTRIBUTIVE = 0x111;
    public static final int ENSURE_CONTRIBUTION = 0x112;

    public AddCoborrowerPresenter(AddCoborrowerContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if(!compositeSubscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void addCoborrower(String certAddressArea, String certAddressCity, String certAddressProvince,
                              String certDetailAddress, String certNo, String mobilePhone, String name,
                              String orderNo, String relation) {
        Params params = new Params();
        params.put("certAddressArea", certAddressArea);
        params.put("certAddressCity", certAddressCity);
        params.put("certAddressProvince", certAddressProvince);
        params.put("certDetailAddress", certDetailAddress);
        params.put("certNo", certNo);
        params.put("certType", "01");
        params.put("mobilePhone", mobilePhone);
        params.put("category", name);
        params.put("orderNo", orderNo);
        params.put("personType", "01");
        params.put("relation", relation);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().insertDebtorByAPP(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<AddCoborrowerBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<AddCoborrowerBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.data, ADD_COBORROWER));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void contributiveTypeJudge(String idCardNo, String mobile, String orderNo, String realName) {
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
                        mView.onSuccess(M.createMessage(httpResponse.data, CONTRIBUTIVE));
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
                        mView.ensureFailure(errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
