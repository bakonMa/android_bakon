package com.bakon.android.ui.activity.welcome;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;

import com.bakon.android.BuildConfig;
import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.EventConfig;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.eventbus.Event;
import com.bakon.android.di.components.DaggerActivityComponent;
import com.bakon.android.di.modules.ActivityModule;
import com.bakon.android.ui.activity.fragment.MainActivity;
import com.bakon.android.ui.activity.login.LoginActivity;
import com.bakon.android.ui.base.BaseActivity;
import com.bakon.android.ui.bean.AppUpdateBean;
import com.bakon.android.ui.bean.DownloadProgressBean;
import com.bakon.android.ui.contact.TranslucentContact;
import com.bakon.android.ui.presenter.TranslucentPresenter;
import com.bakon.android.utils.ActivityUtil;
import com.bakon.android.utils.MD5Util;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.utils.U;
import com.bakon.android.widget.dialog.AppUpdateDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;

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
        if (MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.LAST_ENTER_CODE, 1) != BuildConfig.VERSION_CODE) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
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
                .applicationComponent(MyApplication.getAppComponent())
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
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            String apkDirPath;
                            if (Environment.MEDIA_MOUNTED.equals(MyApplication.getAppComponent().dataRepo().storage().externalRootDirState())) {
                                apkDirPath = MyApplication.getAppComponent().dataRepo().storage().externalPublicDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getPackageName() + File.separator + "install";
                            } else {
                                apkDirPath = MyApplication.getAppComponent().dataRepo().storage().internalCustomDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getPackageName() + File.separator + "install";
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
                MyApplication.getInstance().managerRepository.actMgr().finishAllActivity();
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
            case TranslucentPresenter.CHECK_UPDATE://升级提醒
                long systemClock = System.currentTimeMillis();
                //最后检测时间
                long lastCheckUpdateTime = MyApplication.getAppComponent().dataRepo().appSP().getLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME);
                AppUpdateBean appUpdateBean = (AppUpdateBean) message.obj;
                boolean isForce = appUpdateBean.isforced == 1;
                //强制 || 第一次 || （非强制 && 时间超过1天）  都弹出升级提示
                if (isForce || lastCheckUpdateTime == 0 || (!isForce && systemClock >= lastCheckUpdateTime + 1 * 24 * 60 * 60 * 1000)) {
                    //提示后 关系最新时间
                    MyApplication.getAppComponent().dataRepo().appSP().setLong(SPConfig.SP_LONG_LASTCHECKUPDATE_TIME_NAME, systemClock);
                    showUpdateDialog((AppUpdateBean) message.obj);
                } else {
                    jumpToMain();
                }
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
