package com.renxin.doctor.activity.injection.modules;

import android.content.Context;
import android.content.res.Resources;

import com.renxin.doctor.activity.BuildConfig;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.http.APIModule;
import com.renxin.doctor.activity.data.localdata.StorageOperator;
import com.renxin.doctor.activity.injection.qualifiers.ApplicationContext;
import com.renxin.doctor.activity.manager.ActManager;
import com.renxin.doctor.activity.manager.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@Module(includes = APIModule.class)
public final class ApplicationModule {

    private DocApplication mApplication;

    public ApplicationModule(DocApplication application) {
        mApplication = application;
    }

    @Singleton
    @ApplicationContext
    @Provides
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    public DocApplication provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Resources provideResources() {
        return mApplication.getResources();
    }

    @Singleton
    @Provides
    public RefWatcher provideRefWatcher() {
        if (LeakCanary.isInAnalyzerProcess(mApplication)) {
            return null;
        }
        if (BuildConfig.DEBUG) {
            return LeakCanary.install(mApplication);
        }
        return null;
    }

    @Singleton
    @Provides
    public ActManager provideActManager() {
        return new ActManager(mApplication);
    }

    @Singleton
    @Provides
    public CrashHandler provideCrashHandler(ActManager actManager, StorageOperator storageOperator) {
        return new CrashHandler(mApplication, actManager, storageOperator);
    }

    @Singleton
    @Provides
    public HashMap<String, Object> provideHashMap() {
        return new HashMap();
    }

}
