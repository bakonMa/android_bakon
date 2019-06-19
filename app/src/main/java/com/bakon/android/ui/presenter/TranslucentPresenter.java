package com.bakon.android.ui.presenter;

import com.bakon.android.BuildConfig;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.http.HttpAPIWrapper;
import com.bakon.android.data.http.Params;
import com.bakon.android.ui.contact.TranslucentContact;
import com.bakon.android.data.eventbus.MSG;
import com.bakon.android.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by table on 2017/11/29.
 * description:
 */
public class TranslucentPresenter implements TranslucentContact.Presenter {

    private TranslucentContact.View mView;

    private Disposable downloadApkDisposable, disposable;
    public static final int CHECK_UPDATE = 0x111;

    public TranslucentPresenter(TranslucentContact.View mView) {
        this.mView = mView;
    }

    @Override
    public void unsubscribe() {
        if (downloadApkDisposable != null && !downloadApkDisposable.isDisposed()) {
            downloadApkDisposable.dispose();
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * check版本
     */
    @Override
    public void checkVersion() {
        //上次检测是否强制更新
        boolean initIsForceUpdate = MyApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, false);

        Params params = new Params();
        params.put("version_code", BuildConfig.VERSION_CODE);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        disposable = Observable.zip(
                Observable.timer(1, TimeUnit.SECONDS),//启动动画 1秒
                MyApplication.getAppComponent().dataRepo().http().provideHttpAPI().appUpdateCheck(params),
                (aLong, httpResponse) -> httpResponse)
                .compose(HttpAPIWrapper.SCHEDULERS_TRANSFORMER())
                .compose(mView.toLifecycle())
                .subscribe(
                        httpResponse -> {
                            if (httpResponse.data != null) {
                                int netVersionCode = httpResponse.data.version_code;//最新版本code
                                boolean isForced = httpResponse.data.isforced == 1;//是否强制

                                //检查成功，更新检查成功后是否强制升级
                                MyApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, isForced);
//                                        MyApplication.getAppComponent().dataRepo().appSP().setLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, SystemClock.currentThreadTimeMillis());

                                //手机中版本大于等于网络中请求到的版本
                                if (BuildConfig.VERSION_CODE >= netVersionCode) {
                                    mView.jumpToMain();
                                } else {
                                    //开启对话框
                                    mView.onSuccess(MSG.createMessage(httpResponse.data, CHECK_UPDATE));
                                }
                            }
                            //请求失败，无结果
                            else {
                                //非强制更新状态下，不更新请求结果，直接进去下个页面
                                if (!initIsForceUpdate) {
                                    mView.jumpToMain();
                                }
                                //否则，停留在本页
                                else {
                                    ToastUtil.showShort("网络错误，请退出重试");
                                }
                            }
                        },
                        //发生异常，无结果
                        throwable -> {
                            //非强制更新状态下，不更新请求结果，直接进去下个页面
                            if (!initIsForceUpdate) {
                                mView.jumpToMain();
                            }
                            //否则，停留在本页
                            else {
                                ToastUtil.showShort("网络错误，请退出重试");
                            }
                            throwable.printStackTrace();
                        }
                );
    }

    @Override
    public void downloadApk(String downloadUrl, String destLocaLPath, String sourceMD5, boolean force) {
        if (downloadApkDisposable != null && !downloadApkDisposable.isDisposed()) {
            downloadApkDisposable.dispose();
        }
        //progress: progressCallback
        //success: mView.apkDownloadSuccess(downloadedApkPath , sourceMD5)
        //fail: mView.apkDownloadFailed(throwable)
        downloadApkDisposable = MyApplication.getAppComponent().dataRepo().http().downloadApk(downloadUrl, destLocaLPath, downloadUrl)
                .compose(mView.toLifecycle())
                .subscribe(
                        downloadRespBean -> mView.apkDownloadSuccess(downloadRespBean.localPath, sourceMD5),
                        throwable -> {
                            mView.apkDownloadFailed(throwable);
                            throwable.printStackTrace();
                        }

                );
    }

    @Override
    public void cancelDownload() {
        if (downloadApkDisposable != null && !downloadApkDisposable.isDisposed()) {
            downloadApkDisposable.dispose();
        }
    }

}
