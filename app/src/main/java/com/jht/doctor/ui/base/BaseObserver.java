package com.jht.doctor.ui.base;

import android.util.Log;

import com.bumptech.glide.load.HttpException;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.widget.dialog.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import rx.Observer;

/**
 * Created by table on 2017/11/22.
 * description:公共返回处理
 */
public abstract class BaseObserver<T extends HttpResponse> implements Observer<T> {
    private LoadingDialog mDialog;

    public BaseObserver(LoadingDialog dialog) {
        this.mDialog = dialog;
    }

    @Override
    public void onCompleted() {
//        if (mDialog != null)
//            mDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        if (mDialog != null) {
            if (throwable instanceof ConnectException) {
                mDialog.dismiss();
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("网络错误，请检查网络");
            } else if (throwable instanceof HttpException) {
                mDialog.dismiss();
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("网络异常,请求失败");
            } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                mDialog.dismiss();
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("连接超时,请稍后重试");
            } else {
                mDialog.error("系统异常");
            }
        }
        Log.e("error", throwable.getMessage());
    }


    @Override
    public void onNext(T t) {
        if (mDialog != null)
            mDialog.dismiss();
        if (t.success) {
            onSuccess(t);
        } else {
            onError(t.errorCode, t.errorMsg);
        }


    }

    public abstract void onSuccess(T t);

    public abstract void onError(String errorCode, String errorMsg);
}
