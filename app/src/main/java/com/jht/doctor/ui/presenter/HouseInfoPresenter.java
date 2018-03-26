package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.widget.dialog.LoadingDialog;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.ui.bean.ApplyInfoBean;
import com.jht.doctor.ui.bean.HouseInfoResponse;
import com.jht.doctor.ui.contact.HouseInfoContact;
import com.jht.doctor.utils.M;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/27.
 * description:
 */

public class HouseInfoPresenter implements HouseInfoContact.Presenter {
    private HouseInfoContact.View mView;

    private CompositeSubscription compositeSubscription;

    private LoadingDialog mDialog;

    public static final int COMMIT_HOUSE_INFO = 0x110;
    public static final int APPLY_INFO = 0x112;

    public static final String COMMIT_ERROR = "1";
    public static final String REQUEST_ERROR = "2";

    public HouseInfoPresenter(HouseInfoContact.View mView) {
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
    public void commitHouseInfo(String houseType, double houseArea, String communityName, String provinceCode,
                                String cityCode, String areaCode, String detailAddress, String hasLoan,
                                String loanOrg, double loanAmt,double tradingAmt,double monthRentalAmount) {
        Params params = new Params();
        params.put("houseType", houseType);
        params.put("houseArea", houseArea);
        params.put("communityName", communityName);
        params.put("provinceCode", provinceCode);
        params.put("cityCode", cityCode);
        params.put("areaCode", areaCode);
        params.put("detailAddress", detailAddress);
        params.put("hasLoan", hasLoan);
        params.put("loanOrg", loanOrg);
        params.put("loanAmt", loanAmt);
        params.put("tradingAmt", tradingAmt);
        params.put("monthRentalAmount", monthRentalAmount);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().commitHouseInfo(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null)
                        mDialog.show();
                }).subscribe(new BaseObserver<HttpResponse<HouseInfoResponse>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<HouseInfoResponse> houseInfoResponseHttpResponse) {
                        mView.onSuccess(M.createMessage(houseInfoResponseHttpResponse.data, COMMIT_HOUSE_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(COMMIT_ERROR, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }


    @Override
    public void requestInfo() {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getApplyInfo())
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<ApplyInfoBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<ApplyInfoBean> applyInfoBeanHttpResponse) {
                        mView.onSuccess(M.createMessage(applyInfoBeanHttpResponse.data, APPLY_INFO));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(REQUEST_ERROR, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
