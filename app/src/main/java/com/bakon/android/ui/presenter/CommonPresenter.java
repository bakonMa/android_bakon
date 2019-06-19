package com.bakon.android.ui.presenter;

import com.bakon.android.application.MyApplication;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.data.eventbus.MSG;
import com.bakon.android.data.http.Params;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.ui.base.BaseObserver;
import com.bakon.android.ui.base.BasePresenter;
import com.bakon.android.ui.base.BaseView;
import com.bakon.android.utils.LogUtil;
import com.bakon.android.widget.dialog.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public class CommonPresenter implements BasePresenter {

    private LoadingDialog mDialog;
    private BaseView mView;

    private CompositeDisposable mDisposable;

    public static final int GET_MOMBNO_OK = 0x110;
    public static final int ADD_CHAT_RECORD_OK = 0x112;//添加交互记录

    public CommonPresenter(BaseView view) {
        if (view != null) {
            this.mView = view;
            mDialog = new LoadingDialog(view.provideContext());
        }
        mDisposable = new CompositeDisposable();
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

    //添加记录
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
                    public void onSuccess(HttpResponse<String> personalBeanHttpResponse) {
                        LogUtil.d("CommonPresenter addChatRecord ok");
                        mView.onSuccess(MSG.createMessage(personalBeanHttpResponse.data, ADD_CHAT_RECORD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        LogUtil.d("CommonPresenter addChatRecord onError:" + errorMsg);
//                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

}
