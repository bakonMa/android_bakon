package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.http.HttpAPIWrapper;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.ui.contact.TranslucentContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/29.
 * description:
 */
public class TranslucentPresenter implements TranslucentContact.Presenter {

    private TranslucentContact.View mView;

    private CompositeSubscription compositeSubscription;
    private Subscription downloadApkSubscription;

    public static final int CHECK_UPDATE = 0x111;

    public TranslucentPresenter(TranslucentContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()) {
            downloadApkSubscription.unsubscribe();
        }
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    /**
     * check版本
     */
    @Override
    public void checkVersion() {
        //上次检测是否强制更新
        boolean initIsForceUpdate = DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, false);

        Subscription subscription;

        Params params = new Params();
        params.put("version_code", BuildConfig.VERSION_CODE);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        subscription =
                Observable.zip(
                        Observable.timer(1, TimeUnit.SECONDS),//启动动画 1秒
                        DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().appUpdateCheck(params),
                        (aLong, httpResponse) -> httpResponse)
                        .compose(HttpAPIWrapper.SCHEDULERS_TRANSFORMER())
                        .compose(mView.toLifecycle())
                        .subscribe(
                                httpResponse -> {
                                    if (httpResponse.data != null) {
                                        int netVersionCode = httpResponse.data.version_code;//最新版本code
                                        boolean isForced = httpResponse.data.isforced == 1;//是否强制

                                        //检查成功，更新检查成功后是否强制升级
                                        DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, isForced);
//                                        DocApplication.getAppComponent().dataRepo().appSP().setLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, SystemClock.currentThreadTimeMillis());

                                        //手机中版本大于等于网络中请求到的版本
                                        if (BuildConfig.VERSION_CODE >= netVersionCode) {
                                            mView.jumpToMain();
                                        } else {
                                            //开启对话框
                                            mView.onSuccess(M.createMessage(httpResponse.data, CHECK_UPDATE));
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

        compositeSubscription.add(subscription);
    }

    @Override
    public void downloadApk(String downloadUrl, String destLocaLPath, String sourceMD5, boolean force) {
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()) {
            downloadApkSubscription.unsubscribe();
        }
        //progress: progressCallback
        //success: mView.apkDownloadSuccess(downloadedApkPath , sourceMD5)
        //fail: mView.apkDownloadFailed(throwable)
        downloadApkSubscription =
                DocApplication.getAppComponent().dataRepo().http().downloadApk(downloadUrl, destLocaLPath, downloadUrl)
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
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()) {
            downloadApkSubscription.unsubscribe();
        }
    }

}
