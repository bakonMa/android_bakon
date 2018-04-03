package com.jht.doctor.ui.base;

import android.util.Log;

import com.bumptech.glide.load.HttpException;
import com.jht.doctor.data.http.ApiException;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.utils.ToastUtil;
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
        if (mDialog != null)
            mDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        if (mDialog != null) {
            if (throwable instanceof ConnectException) {
                mDialog.dismiss();
                ToastUtil.show("网络错误，请检查网络");
            } else if (throwable instanceof HttpException) {
                mDialog.dismiss();
                ToastUtil.show("网络异常,请求失败");
            } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                mDialog.dismiss();
                ToastUtil.show("连接超时,请稍后重试");
            } else if (throwable instanceof ApiException) {
                mDialog.dismiss();
                if (((ApiException) throwable).getCode().equals(ApiException.ERROR_API_1001)
                        || ((ApiException) throwable).getCode().equals(ApiException.ERROR_API_1002)) {
                    ToastUtil.show(throwable.getMessage());
                }
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
        if (t.code.equals("1")) {//todo  网络是否成功
            onSuccess(t);
        } else {
            onError(t.code, t.msg);
        }


    }

    public abstract void onSuccess(T t);

    public abstract void onError(String errorCode, String errorMsg);
}
