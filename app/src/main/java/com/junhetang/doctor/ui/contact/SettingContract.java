package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.bean.AppUpdateBean;

/**
 * Created by zhaoyun on 2018/1/12.
 */
public interface SettingContract {

    interface View extends BaseView<Presenter> {
        void showUpdateDialog(AppUpdateBean appUpdateBean);
        void apkDownloadSuccess(String localApkPath , String sourceMD5);
        void apkDownloadFailed(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        //判断是否有交易密码
        void tradePwdStatus();
        //检查更新
        void checkUpdate();
        //下载apk文件
        void downloadApk(String downloadUrl , String destLocaLPath , String sourceMD5 , boolean force);
        //取消下载
        void cancelDownload();
    }

}