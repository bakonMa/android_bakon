package com.jht.doctor.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.http.APIModule;
import com.jht.doctor.injection.components.ApplicationComponent;
import com.jht.doctor.injection.components.DaggerApplicationComponent;
import com.jht.doctor.injection.modules.ApplicationModule;
import com.jht.doctor.manager.GreenDaoManager;
import com.jht.doctor.manager.ManagerRepository;
import com.jht.doctor.nim.NimManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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
        //内存泄漏检测
        refWatcher = setupLeakCanary();
        //db 初始化
        GreenDaoManager.getInstance();
        //基础数据
        if (TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_KEY_BASE_CONFIG, ""))) {
            mApplicationComponent.dataRepo().appSP().setString(SPConfig.SP_KEY_BASE_CONFIG, SPConfig.BASE_CONFIG);
        }
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
