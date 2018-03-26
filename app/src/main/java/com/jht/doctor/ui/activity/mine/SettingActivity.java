package com.jht.doctor.ui.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean.AppUpdateBean;
import com.jht.doctor.ui.bean.DownloadProgressBean;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.contact.SettingContract;
import com.jht.doctor.ui.presenter.SettingPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.MD5Util;
import com.jht.doctor.widget.RelativeWithText;
import com.jht.doctor.widget.dialog.AppUpdateDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * mayakun 2017/11/16
 * 设置画面
 */
public class SettingActivity extends BaseActivity implements SettingContract.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Inject
    SettingPresenter mPresenter;
    @BindView(R.id.tv_reset_password)
    RelativeWithText tvResetPassword;
    @BindView(R.id.tv_check_update)
    RelativeWithText tvCheckUpdate;

    private OtherBean otherBean;
    private AppUpdateDialog appUpdateDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initToolbar();
        //查询是否设置过交易密码
        mPresenter.tradePwdStatus();
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
        if (downloadApkEvent.getCode() == EventConfig.REFRESH_APK_DOWNLOAD_PROGRESS){
            if (appUpdateDialog != null && appUpdateDialog.isShowing()){
                double doubleProgress = ((double) downloadApkEvent.getData().bytesRead) / ((double) downloadApkEvent.getData().contentLength);
                appUpdateDialog.setProgress((int) (doubleProgress * 10000));
            }
        }
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("设置")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }


    @OnClick({R.id.tv_reset_password, R.id.tv_check_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_password:
                if (otherBean == null) {
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("数据异常，请退出后重试");
                    return;
                }
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("isrest", otherBean.tradePwdStatus);
                startActivity(intent);
                finish();
                break;

            case R.id.tv_check_update:
                mPresenter.checkUpdate();
                break;

            default:
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case SettingPresenter.SETTING_PWD_STATUS:
                    otherBean = (OtherBean) message.obj;
                    Log.e("tag", otherBean.tradePwdStatus + "");
                    if (otherBean != null) {
                        tvResetPassword.setTitleText(otherBean.tradePwdStatus ?
                                "重置交易密码" : "设置交易密码");
                    }
                    break;

                case SettingPresenter.SETTING_CHECK_UPDATE_STATUS:
                    showUpdateDialog((AppUpdateBean) message.obj);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showUpdateDialog(AppUpdateBean appUpdateBean) {
        AppUpdateDialog.StartDownloadingListener startDownloadingListener = (downloadUrl, netMD5, force) -> {
            RxPermissions rxPermissions = new RxPermissions(SettingActivity.this);
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
                                    ActivityUtil.installApk(SettingActivity.this, apkFile, getPackageName() + ".fileprovider");
                                } else {
                                    apkFile.delete();
                                    if (appUpdateDialog != null){
                                        appUpdateDialog.setProgress(0);
                                        appUpdateDialog.switchViewState(true);
                                    }
                                    mPresenter.downloadApk(downloadUrl, apkFile.getAbsolutePath(), netMD5, force);
                                }
                            } else {
                                if (appUpdateDialog != null){
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
            if (force){
                DocApplication.getInstance().managerRepository.actMgr().finishAllActivity();
            }
        };
        appUpdateDialog = new AppUpdateDialog(this , R.style.UpdateDialogTheme , appUpdateBean , startDownloadingListener , cancelListener);
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
        }else {
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

}
