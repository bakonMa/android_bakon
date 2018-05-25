package com.junhetang.doctor.ui.presenter;

import android.text.TextUtils;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.nim.UserPreferences;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

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
    public static final int ADD_CHAT_RECORD_OK = 0x112;//添加交互记录

    public CommonPresenter(BaseView view) {
        if (view != null) {
            this.mView = view;
            mDialog = new LoadingDialog(view.provideContext());
        }
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

    //添加记录
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
                        LogUtil.d("CommonPresenter addChatRecord ok");
                        mView.onSuccess(M.createMessage(personalBeanHttpResponse.data, ADD_CHAT_RECORD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("CommonPresenter addChatRecord onError:" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

}
