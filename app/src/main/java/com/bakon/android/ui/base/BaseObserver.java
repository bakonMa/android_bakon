package com.bakon.android.ui.base;

import com.bumptech.glide.load.HttpException;
import com.bakon.android.data.http.ApiException;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.widget.dialog.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;


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
    public void onComplete() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof ConnectException) {
            showErroeMsg("网络不可用，请检查网络设置");
        } else if (throwable instanceof HttpException) {
            showErroeMsg("网络异常，请求失败");
        } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
            showErroeMsg("连接超时，请稍后重试");
        } else if (throwable instanceof ApiException) {
            if (((ApiException) throwable).getCode().equals(ApiException.ERROR_API_1001)
                    || ((ApiException) throwable).getCode().equals(ApiException.ERROR_API_1002)) {
                ToastUtil.show(throwable.getMessage());
            }
            if (mDialog != null) {
                mDialog.dismiss();
            }
        } else {
            showErroeMsg("系统异常，请稍后重试");
        }
    }

    private void showErroeMsg(String str) {
        if (mDialog != null) {
            mDialog.error(str);
        } else {
            ToastUtil.show(str);
        }
    }

    @Override
    public void onNext(T t) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (t.code.equals("1")) {//todo  网络是否成功
            onSuccess(t);
        } else {
            onError(t.code, t.msg);
        }


    }

    public abstract void onSuccess(T t);

    public abstract void onError(String errorCode, String errorMsg);
}
