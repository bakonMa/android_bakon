package com.jht.doctor.ui.activity.welcome;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.home.MainActivity;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean.AppUpdateBean;
import com.jht.doctor.ui.bean.DownloadProgressBean;
import com.jht.doctor.ui.bean.RepaymentHomeBean;
import com.jht.doctor.ui.contact.TranslucentContact;
import com.jht.doctor.ui.presenter.TranslucentPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.MD5Util;
import com.jht.doctor.widget.dialog.AppUpdateDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import javax.inject.Inject;

/**
 * @author Tang
 */
public class TranslucentActivity extends BaseActivity implements TranslucentContact.View {

    @Inject
    TranslucentPresenter mPresenter;

    private AppUpdateDialog appUpdateDialog;

    @Override
    protected int provideRootLayout() {
        return 0;
    }

    @Override
    protected void initView() {
        checkFirstEnter();

    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        mPresenter.unsubscribe();
        if (appUpdateDialog != null) {
            appUpdateDialog.dismiss();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadApkEvent(Event<DownloadProgressBean> downloadApkEvent) {
        if (downloadApkEvent.getCode() == EventConfig.REFRESH_APK_DOWNLOAD_PROGRESS) {
            if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
                double doubleProgress = ((double) downloadApkEvent.getData().bytesRead) / ((double) downloadApkEvent.getData().contentLength);
                appUpdateDialog.setProgress((int) (doubleProgress * 10000));
            }
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private void checkFirstEnter() {
        if (DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.FIRST_ENTER, true)) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            //初始配置数据
            mPresenter.getBaseCinfig();
            //todo 判断是否有token
//            if (StringUtils.isEmpty(CustomerApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
//                Observable.timer(1000, TimeUnit.MILLISECONDS)
//                        .subscribe(aLong -> {
//                            startActivity(new Intent(TranslucentActivity.this, HomeLoanActivity.class));
//                            finish();
//                        });
//
//            } else {
//                mPresenter.getRepayment();
//            }

            mPresenter.subscribe();
        }
    }

    @Override
    public void jumpToMain() {
        startActivity(new Intent(TranslucentActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showUpdateDialog(AppUpdateBean appUpdateBean) {
        AppUpdateDialog.StartDownloadingListener startDownloadingListener = (downloadUrl, netMD5, force) -> {
            RxPermissions rxPermissions = new RxPermissions(TranslucentActivity.this);
            rxPermissions.setLogging(BuildConfig.DEBUG);
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            String apkDirPath;
                            if (Environment.MEDIA_MOUNTED.equals(DocApplication.getAppComponent().dataRepo().storage().externalRootDirState())) {
                                apkDirPath = DocApplication.getAppComponent().dataRepo().storage().externalPublicDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getPackageName() + File.separator + "install";
                            } else {
                                apkDirPath = DocApplication.getAppComponent().dataRepo().storage().internalCustomDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getPackageName() + File.separator + "install";
                            }
                            File apkDir = new File(apkDirPath);
                            if (!apkDir.exists()) {
                                apkDir.mkdirs();
                            }
                            File apkFile = new File(apkDir, getPackageName() + ".apk");
                            if (apkFile.exists()) {
                                String localMD5 = MD5Util.md5(apkFile);
                                if (localMD5.equals(netMD5)) {
                                    ActivityUtil.installApk(TranslucentActivity.this, apkFile, getPackageName() + ".fileprovider");
                                } else {
                                    apkFile.delete();
                                    if (appUpdateDialog != null) {
                                        appUpdateDialog.setProgress(0);
                                        appUpdateDialog.switchViewState(true);
                                    }
                                    mPresenter.downloadApk(downloadUrl, apkFile.getAbsolutePath(), netMD5, force);
                                }
                            } else {
                                if (appUpdateDialog != null) {
                                    appUpdateDialog.setProgress(0);
                                    appUpdateDialog.switchViewState(true);
                                }
                                mPresenter.downloadApk(downloadUrl, apkFile.getAbsolutePath(), netMD5, force);
                            }
                        } else {
                            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请求权限失败");
                        }
                    });
        };
        AppUpdateDialog.CancelListener cancelListener = force -> {
            mPresenter.cancelDownload();
            if (force) {
                DocApplication.getInstance().managerRepository.actMgr().finishAllActivity();
            } else {
                jumpToMain();
            }
        };
        appUpdateDialog = new AppUpdateDialog(this, R.style.UpdateDialogTheme, appUpdateBean, startDownloadingListener, cancelListener);
        appUpdateDialog.show();
    }

    @Override
    public void apkDownloadSuccess(String localApkPath, String sourceMD5) {
        String localMD5 = MD5Util.md5(new File(localApkPath));
        if (localMD5.equals(sourceMD5)) {
            if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
                ActivityUtil.installApk(this, new File(localApkPath), getPackageName() + ".fileprovider");
                appUpdateDialog.switchViewState(false);
                appUpdateDialog.setProgress(0);
            }
        } else {
            if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
                appUpdateDialog.setUpdateText("点击重试");
                appUpdateDialog.switchViewState(false);
                appUpdateDialog.setProgress(0);
            }
        }
    }

    @Override
    public void apkDownloadFailed(Throwable throwable) {
        if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
            appUpdateDialog.setUpdateText("点击重试");
            appUpdateDialog.switchViewState(false);
            appUpdateDialog.setProgress(0);
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
//        startActivity(new Intent(TranslucentActivity.this, HomeLoanActivity.class));
        finish();
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case TranslucentPresenter.REPAYMENT:
                RepaymentHomeBean repaymentHomeBean = (RepaymentHomeBean) message.obj;
                if (repaymentHomeBean == null) {
//                    startActivity(new Intent(this, HomeLoanActivity.class));
                    finish();
                    return;
                }
                if (repaymentHomeBean.isRepaymentStatus()) {
//                    startActivity(new Intent(this, HomeRepaymentActivity.class));
                } else {
//                    startActivity(new Intent(this, HomeLoanActivity.class));
                }
                finish();
                break;

            case TranslucentPresenter.CHECK_UPDATE:
                showUpdateDialog((AppUpdateBean) message.obj);
                break;

            default:
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

}
