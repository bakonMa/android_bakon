package com.junhetang.doctor.ui.activity.welcome;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.activity.fragment.MainActivity;
import com.junhetang.doctor.ui.activity.login.LoginActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.AppUpdateBean;
import com.junhetang.doctor.ui.bean.DownloadProgressBean;
import com.junhetang.doctor.ui.contact.TranslucentContact;
import com.junhetang.doctor.ui.presenter.TranslucentPresenter;
import com.junhetang.doctor.utils.ActivityUtil;
import com.junhetang.doctor.utils.MD5Util;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.dialog.AppUpdateDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import javax.inject.Inject;

/**
 * 第一个页面
 * TranslucentActivity
 * Create at 2018/4/3 下午4:04 by mayakun
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
        if (DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.FIRST_ENTER, true)) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            // todo 测试
//            DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.FIRST_ENTER, true);
            //版本检查
            mPresenter.checkVersion();
        }
    }

    @Override
    protected void onDestroy() {
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
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void jumpToMain() {
        if (U.isNoToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void showUpdateDialog(AppUpdateBean appUpdateBean) {
        AppUpdateDialog.StartDownloadingListener startDownloadingListener = (downloadUrl, netMD5, force) -> {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.setLogging(BuildConfig.DEBUG);
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                            //已存在，校验md5
                            if (apkFile.exists()) {
                                String localMD5 = MD5Util.md5(apkFile);
                                //md5相同，直接安装
                                if (localMD5.equals(netMD5)) {
                                    ActivityUtil.installApk(TranslucentActivity.this, apkFile, getPackageName() + ".fileprovider");
                                } else {
                                    //不同删除apk 重新下载
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
                            ToastUtil.show("请求权限失败");
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
        finish();
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case TranslucentPresenter.CHECK_UPDATE:
//                long systemClock = SystemClock.currentThreadTimeMillis();
//                //上次检测是否强制更新
//                boolean initIsForceUpdate = DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_BOOL_LASTCHECK_FORCEUPDATE_NAME, false);
//                //最后检测时间
//                long lastCheckUpdateTime = DocApplication.getAppComponent().dataRepo().appSP().getLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, systemClock);
//                if (systemClock == lastCheckUpdateTime || (!initIsForceUpdate && systemClock >= lastCheckUpdateTime + 7 * 24 * 60 * 60 * 1000) || initIsForceUpdate) {
                showUpdateDialog((AppUpdateBean) message.obj);
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean useButterKnife() {
        return false;
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
