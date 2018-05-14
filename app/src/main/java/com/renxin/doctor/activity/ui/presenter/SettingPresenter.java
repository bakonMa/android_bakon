package com.renxin.doctor.activity.ui.presenter;

import android.os.SystemClock;

import com.renxin.doctor.activity.BuildConfig;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.config.SPConfig;
import com.renxin.doctor.activity.data.http.HttpAPIWrapper;
import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.base.BaseObserver;
import com.renxin.doctor.activity.ui.bean.AppUpdateBean;
import com.renxin.doctor.activity.ui.bean.OtherBean;
import com.renxin.doctor.activity.ui.contact.SettingContract;
import com.renxin.doctor.activity.utils.M;
import com.renxin.doctor.activity.widget.dialog.LoadingDialog;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhaoyun on 2018/1/12.
 */
public class SettingPresenter implements SettingContract.Presenter {

    private final SettingContract.View mView;
    private CompositeSubscription mSubscription;
    private Subscription downloadApkSubscription;
    private LoadingDialog mdialog;
    public static final int SETTING_PWD_STATUS = 0x111;
    public static final int SETTING_CHECK_UPDATE_STATUS = 0x112;

    @Inject
    public SettingPresenter(SettingContract.View view) {
        this.mView = view;
        mdialog = new LoadingDialog(mView.provideContext());
        mSubscription = new CompositeSubscription();

    }

    @Override
    public void unsubscribe() {
        if (mdialog != null) {
            mdialog.dismiss();
        }
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()) {
            downloadApkSubscription.unsubscribe();
        }
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    //判断是否有交易密码
    @Override
    public void tradePwdStatus() {
        Params params = new Params();
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().tradePwdStatus(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mdialog != null) mdialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<OtherBean>>(mdialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> resultResponse) {
                        mView.onSuccess(M.createMessage(resultResponse.data, SETTING_PWD_STATUS));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void checkUpdate() {
        Params params = new Params();
        params.put("version_code", BuildConfig.VERSION_CODE);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().appUpdateCheck(params)
                        .compose(HttpAPIWrapper.SCHEDULERS_TRANSFORMER())
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mdialog != null) mdialog.show();
                        })
                        .subscribe(new BaseObserver<HttpResponse<AppUpdateBean>>(mdialog) {
                            @Override
                            public void onSuccess(HttpResponse<AppUpdateBean> resultResponse) {
                                //请求成功，有结果
                                if (resultResponse.data != null) {
                                    int netVersionCode = resultResponse.data.version_code;
                                    boolean isForce = resultResponse.data.isforced == 1;

                                    //检查成功，更新检查成功后得到的当前时间和是否强制更新(强制更新标示符以及最低版本)
                                    DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, isForce);
                                    DocApplication.getAppComponent().dataRepo().appSP().setLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, SystemClock.currentThreadTimeMillis());

                                    //手机中版本大于等于网络中请求到的版本
                                    if (BuildConfig.VERSION_CODE >= netVersionCode) {
                                        //提示已是最新版本
                                        mView.onError("", "已是最新版本");
                                    } else {
                                        mView.onSuccess(M.createMessage(resultResponse.data, SETTING_CHECK_UPDATE_STATUS));
                                    }
                                }
                            }

                            @Override
                            public void onError(String errorCode, String errorMsg) {
                                //提示检查更新失败
                                mView.onError(errorCode, "检查更新失败");
                            }
                        });
        mSubscription.add(subscription);
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
        if (downloadApkSubscription != null && !downloadApkSubscription.isUnsubscribed()) {
            downloadApkSubscription.unsubscribe();
        }
    }

}
