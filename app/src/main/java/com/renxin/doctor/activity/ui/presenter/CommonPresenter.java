package com.renxin.doctor.activity.ui.presenter;

import android.text.TextUtils;

import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.nim.UserPreferences;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.bean.OtherBean;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class CommonPresenter implements BasePresenter {

    private LoadingDialog mDialog;
    private BaseView mView;

    private CompositeSubscription compositeSubscription;

    public static final int GET_MOMBNO_OK = 0x110;

    public CommonPresenter(BaseView view) {
        this.mView = view;
        mDialog = new LoadingDialog(view.provideContext());
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    //accid 获取 memb_no
    public void getMembNo(String accid) {
        String membNo = UserPreferences.getMembNoByAccid(accid);
        if (!TextUtils.isEmpty(membNo)) {
            mView.onSuccess(M.createMessage(membNo, GET_MOMBNO_OK));
            return;
        }
        Params params = new Params();
        params.put("accid", accid);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getMembNo(params))
//                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> httpResponse) {
                        if (httpResponse.data == null || TextUtils.isEmpty(httpResponse.data.memb_no)) {
                            UserPreferences.saveAccidMembNo(accid, "");
                            return;
                        } else {
                            //本地保存
                            UserPreferences.saveAccidMembNo(accid, httpResponse.data.memb_no);
                            mView.onSuccess(M.createMessage(httpResponse.data.memb_no, GET_MOMBNO_OK));
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
