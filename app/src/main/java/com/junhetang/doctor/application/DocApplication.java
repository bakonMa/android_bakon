package com.junhetang.doctor.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.APIModule;
import com.junhetang.doctor.injection.components.ApplicationComponent;
import com.junhetang.doctor.injection.components.DaggerApplicationComponent;
import com.junhetang.doctor.injection.modules.ApplicationModule;
import com.junhetang.doctor.manager.ManagerRepository;
import com.junhetang.doctor.nim.NimManager;
import com.junhetang.doctor.receiver.XGInitManager;
import com.junhetang.doctor.utils.U;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import javax.inject.Inject;

/**
 * DocApplication
 * Create by mayakun at 2018/3/26 下午4:57
 */
public class DocApplication extends Application {

    private static ApplicationComponent mApplicationComponent;

    private static DocApplication mAppInstance;
    //Application为整个应用保存全局的RefWatcher
    private RefWatcher refWatcher;
    public static UMShareAPI umShareAPI;


    @Inject
    public ManagerRepository managerRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        mApplicationComponent = DaggerApplicationComponent.builder()
                .aPIModule(new APIModule(this))
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
        //网易云IM 初始化
        NimManager.getInstance(this);
        //信鸽推送 初始化
        XGInitManager.getInstance(this);
        //bugly isdebug（true/false）
        CrashReport.initCrashReport(getApplicationContext(), "cf03101aef", BuildConfig.DEBUG);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
        CrashReport.setUserId(U.getPhone());//设置手机号为用户id，方便查找
        //内存泄漏检测
        refWatcher = setupLeakCanary();
        //db 初始化
//        GreenDaoManager.getInstance();

        //Umeng分享
        UMConfigure.init(this, HttpConfig.UMENG_APPKEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        umShareAPI = UMShareAPI.get(this);
        PlatformConfig.setWeixin(HttpConfig.WX_APP_ID, HttpConfig.WX_APP_SECRET);
        PlatformConfig.setQQZone(HttpConfig.QQ_APP_ID, HttpConfig.QQ_APP_ID_Key);

    }

    public static DocApplication getInstance() {
        return mAppInstance;
    }

    public static ApplicationComponent getAppComponent() {
        return mApplicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //方法数超过分包
        MultiDex.install(this);
    }

    //内存泄漏检测
    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

}
