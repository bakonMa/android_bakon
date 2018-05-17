package com.junhetang.doctor.injection.modules;

import android.content.Context;
import android.content.res.Resources;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.APIModule;
import com.junhetang.doctor.data.localdata.StorageOperator;
import com.junhetang.doctor.injection.qualifiers.ApplicationContext;
import com.junhetang.doctor.manager.ActManager;
import com.junhetang.doctor.manager.CrashHandler;
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
