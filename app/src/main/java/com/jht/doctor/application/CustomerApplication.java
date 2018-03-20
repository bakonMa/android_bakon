package com.jht.doctor.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.api.http.APIModule;
import com.jht.doctor.injection.components.ApplicationComponent;
import com.jht.doctor.injection.components.DaggerApplicationComponent;
import com.jht.doctor.injection.modules.ApplicationModule;
import com.jht.doctor.manager.GreenDaoManager;
import com.jht.doctor.manager.ManagerRepository;

import javax.inject.Inject;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
public class CustomerApplication extends Application {

    private static ApplicationComponent mApplicationComponent;

    private static CustomerApplication mAppInstance;

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
        //如果要对本地属性进行注入，则还调用一下inject(this);

        //db 初始化
        GreenDaoManager.getInstance();
        if (TextUtils.isEmpty(CustomerApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_KEY_BASE_CONFIG, ""))) {
            mApplicationComponent.dataRepo().appSP().setString(SPConfig.SP_KEY_BASE_CONFIG, SPConfig.BASE_CONFIG);
        }
    }

    public static CustomerApplication getInstance() {
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

}
