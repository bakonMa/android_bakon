package com.renxin.doctor.activity.ui.presenter;

import android.content.pm.PackageManager;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.renxin.doctor.activity.config.SPConfig;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.http.HttpAPIWrapper;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.data.response.MergeBean;
import com.renxin.doctor.activity.ui.bean.RepaymentHomeBean;
import com.renxin.doctor.activity.ui.contact.TranslucentContact;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.ui.bean.ConfigBean;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by table on 2017/11/29.
 * description:
 */
public class TranslucentPresenter implements TranslucentContact.Presenter {

    private TranslucentContact.View mView;

    private CompositeSubscription compositeSubscription;
    private Subscription downloadApkSubscription;

    public static final int REPAYMENT = 0x110;
    public static final int CHECK_UPDATE = 0x111;


    public TranslucentPresenter(TranslucentContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()){
            downloadApkSubscription.unsubscribe();
        }
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void subscribe() {
        long systemClock = SystemClock.currentThreadTimeMillis();
        boolean initIsForceUpdate = DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME , false);
        long lastCheckUpdateTime = DocApplication.getAppComponent().dataRepo().appSP().getLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME , systemClock);

        Subscription subscription;

        if (systemClock == lastCheckUpdateTime || (!initIsForceUpdate && systemClock >= lastCheckUpdateTime + 7 * 24 * 60 * 60 * 1000) || initIsForceUpdate) {
            subscription =
                    Observable.zip(
                            Observable.timer(1, TimeUnit.SECONDS),//启动动画 1秒
                            DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().appUpdateCheck(4),
                            (aLong, httpResponse) -> httpResponse
                    )
                    .compose(HttpAPIWrapper.SCHEDULERS_TRANSFORMER())
                    .compose(mView.toLifecycle())
                    .subscribe(
                            httpResponse -> {
                                if (httpResponse.success && httpResponse.data != null) {
                                    String netVersion = httpResponse.data.version;
                                    String netMinVersion = httpResponse.data.minVersion;
                                    String nowVersion;
                                    try {
                                        nowVersion = mView.provideContext().getPackageManager().getPackageInfo(mView.provideContext().getPackageName(), PackageManager.GET_ACTIVITIES).versionName;
                                    } catch (PackageManager.NameNotFoundException e) {
                                        nowVersion = "1.0";
                                        e.printStackTrace();
                                    }

                                    int netVersionNum = Integer.parseInt(netVersion.replaceAll("\\.", ""));
                                    int minVersionNum = Integer.parseInt(netMinVersion.replaceAll("\\.", ""));
                                    int nowVersionNum = Integer.parseInt(nowVersion.replaceAll("\\.", ""));

                                    boolean isForce = httpResponse.data.forceUpdate == 1 || nowVersionNum < minVersionNum;

                                    //检查成功，更新检查成功后得到的当前时间和是否强制更新(强制更新标示符以及最低版本)

                                    DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, isForce);
                                    DocApplication.getAppComponent().dataRepo().appSP().setLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, SystemClock.currentThreadTimeMillis());

                                    //手机中版本大于等于网络中请求到的版本
                                    if (nowVersionNum >= netVersionNum) {
                                        mView.jumpToMain();
                                    } else {
                                        //开启对话框
                                        httpResponse.data.forceUpdate = isForce ? 1 : 0;
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

                                }
                                throwable.printStackTrace();
                            }
                    );
        }
        //非初始化，但时间没超过7天，直接1秒停顿后进入下个页面
        else {
            subscription =
                    Observable
                            .timer(1, TimeUnit.SECONDS)
                            .compose(HttpAPIWrapper.SCHEDULERS_TRANSFORMER())
                            .compose(mView.toLifecycle())
                            .subscribe(
                                    aLong -> mView.jumpToMain(),
                                    throwable -> {
                                        throwable.printStackTrace();
                                        DocApplication.getInstance().managerRepository.actMgr().finishAllActivity();
                                    }
                            );
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void downloadApk(String downloadUrl, String destLocaLPath, String sourceMD5, boolean force) {
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()){
            downloadApkSubscription.unsubscribe();
        }
        //progress: progressCallback
        //success: mView.apkDownloadSuccess(downloadedApkPath , sourceMD5)
        //fail: mView.apkDownloadFailed(throwable)
        downloadApkSubscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .downloadApk(downloadUrl, destLocaLPath, downloadUrl)
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
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()){
            downloadApkSubscription.unsubscribe();
        }
    }

    @Override
    public void getRepayment() {
        Subscription subscription = Observable.zip(Observable.timer(1000, TimeUnit.MILLISECONDS)
                , DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getHomeRepayment())
                        .compose(mView.toLifecycle())
                , new Func2<Long, HttpResponse<RepaymentHomeBean>, MergeBean<Long, HttpResponse<RepaymentHomeBean>>>() {
                    @Override
                    public MergeBean<Long, HttpResponse<RepaymentHomeBean>> call(Long aLong, HttpResponse<RepaymentHomeBean> repaymentHomeBeanHttpResponse) {
                        return new MergeBean<Long, HttpResponse<RepaymentHomeBean>>(aLong, repaymentHomeBeanHttpResponse);
                    }
                }).subscribe(new Observer<MergeBean<Long, HttpResponse<RepaymentHomeBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.onError("", "");
            }

            @Override
            public void onNext(MergeBean<Long, HttpResponse<RepaymentHomeBean>> mergeBean) {
                mView.onSuccess(M.createMessage(mergeBean.getT2().data, REPAYMENT));
            }
        });
        compositeSubscription.add(subscription);
    }

    /**
     * 请求最新的配置信息，后台约定每次覆盖更新
     */
    @Override
    public void getBaseCinfig() {
        Params params = new Params();
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getConfig(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<ConfigBean>>(null) {
                    @Override
                    public void onSuccess(HttpResponse<ConfigBean> resultResponse) {
                        //保存基础数据json
                        if (resultResponse.data != null) {
                            DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_KEY_BASE_CONFIG, new Gson().toJson(resultResponse.data));
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        //mView.onError(code, msg);
                    }
                });
        compositeSubscription.add(subscription);

    }
}
