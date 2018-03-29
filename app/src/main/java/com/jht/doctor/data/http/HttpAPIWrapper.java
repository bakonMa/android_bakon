package com.jht.doctor.data.http;

import android.content.Intent;
import android.util.Log;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.HttpConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.activity.LoginActivity;
import com.jht.doctor.ui.bean.DownloadRespBean;
import com.jht.doctor.utils.ToastUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public final class HttpAPIWrapper {

    private final HttpAPI mHttpAPI;

    public HttpAPIWrapper(HttpAPI httpAPI) {
        mHttpAPI = httpAPI;
    }

    public HttpAPI provideHttpAPI() {
        return mHttpAPI;
    }

    public Observable<DownloadRespBean> downloadApk(String url, String localPath, String identifier) {
        return mHttpAPI.downloadApk(url, identifier)
                .map(responseBody -> {
                    try {
                        BufferedInputStream bis = new BufferedInputStream(responseBody.byteStream());
                        File file = new File(localPath);
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DownloadRespBean downloadRespBean = new DownloadRespBean();
                    downloadRespBean.localPath = localPath;
                    downloadRespBean.identifier = identifier;
                    return downloadRespBean;
                })
                .compose(SCHEDULERS_TRANSFORMER());
    }

    /**
     * 给任何Http的Observable加上在Activity中运行的线程调度器
     */
    public static <T> Observable.Transformer<T, T> SCHEDULERS_TRANSFORMER() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public <T extends HttpResponse> Observable<T> wrapper(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap((T baseResponse) -> Observable.create(
                        (Observable.OnSubscribe<T>) subscriber -> {
                            //system code
                            if (BuildConfig.DEBUG) {
                                Log.d("token", DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""));
                            }
                            if (baseResponse == null) {
                                subscriber.onCompleted();
                            } else {//success
                                if (baseResponse.code != null
                                        && HttpConfig.NOLOGIN_CODE.equals(baseResponse.code)//未登录 1001
                                        && HttpConfig.SIGN_ERROR_CODE.equals(baseResponse.code)) {//sign错误 1002
                                    //TODO 清空token
                                    DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, "");
                                    ToastUtil.show(baseResponse.msg);
                                    //表示Token失效--重新登录--成功跳HOME
                                    Intent intent = new Intent(DocApplication.getInstance(), LoginActivity.class);
                                    intent.putExtra(LoginActivity.FROM_KEY, LoginActivity.TOKEN_OVERDUE);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    DocApplication.getInstance().startActivity(intent);
                                } else {
                                    subscriber.onNext(baseResponse);
                                }
                            }
                        }
                        )
                ).compose(SCHEDULERS_TRANSFORMER());
    }

}
