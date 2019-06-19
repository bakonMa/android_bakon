package com.bakon.android.data.http;

import android.content.Intent;
import android.util.Log;

import com.bakon.android.BuildConfig;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.localdata.SharePreferencesWrapper;
import com.bakon.android.data.response.HttpResponse;
import com.bakon.android.ui.activity.login.LoginActivity;
import com.bakon.android.ui.bean.DownloadRespBean;
import com.bakon.android.utils.U;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public final class HttpAPIWrapper {

    @Inject
    SharePreferencesWrapper sharePreferencesWrapper;
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
    public static <T> ObservableTransformer<T, T> SCHEDULERS_TRANSFORMER() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public <T extends HttpResponse> Observable<T> wrapper(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap((T baseResponse) -> Observable.create(
                        (ObservableOnSubscribe<T>) subscriber -> {
                            //system code
                            if (BuildConfig.DEBUG) {
                                Log.d("token", sharePreferencesWrapper.getString(SPConfig.SP_STR_TOKEN, ""));
                            }
                            if (baseResponse == null) {
                                subscriber.onComplete();
                            } else {//success
                                if (baseResponse.code == null
                                        || ApiException.ERROR_API_1001.equals(baseResponse.code)
                                        || ApiException.ERROR_API_1002.equals(baseResponse.code)) {//sign错误 1002
                                    //显示异常msg
                                    subscriber.onError(new ApiException(baseResponse.code, baseResponse.msg));
                                    //TODO 清空token
                                    U.logout();
                                    //重新登录-成功跳HOME
                                    Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
                                    intent.putExtra("msg", baseResponse.msg);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyApplication.getInstance().startActivity(intent);
                                } else {
                                    subscriber.onNext(baseResponse);
                                }
                            }
                        }
                        )
                ).compose(SCHEDULERS_TRANSFORMER());
    }

}
