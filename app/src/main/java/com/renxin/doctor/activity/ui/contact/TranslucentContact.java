package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.bean.AppUpdateBean;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/11/29.
 * description:
 */

public interface TranslucentContact {

    interface View extends BaseView<Presenter> {
        void jumpToMain();

        void showUpdateDialog(AppUpdateBean appUpdateBean);

        void apkDownloadSuccess(String localApkPath, String sourceMD5);

        void apkDownloadFailed(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void downloadApk(String downloadUrl, String destLocaLPath, String sourceMD5, boolean force);

        void cancelDownload();

        void checkVersion();
    }
}
