package com.jht.doctor.injection.modules;

import android.content.Context;
import android.content.res.Resources;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.api.http.APIModule;
import com.jht.doctor.data.api.local.storage.StorageOperator;
import com.jht.doctor.injection.qualifiers.ApplicationContext;
import com.jht.doctor.manager.ActManager;
import com.jht.doctor.manager.CrashHandler;
import com.jht.doctor.manager.ToastManager;
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
    public ToastManager provideToastManager() {
        return new ToastManager(mApplication);
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
