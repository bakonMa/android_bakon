package com.bakon.android.di.modules;

import android.content.Context;
import android.content.res.Resources;

import com.bakon.android.BuildConfig;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.localdata.MMKVManager;
import com.bakon.android.data.localdata.SharePreferencesWrapper;
import com.bakon.android.data.localdata.StorageOperator;
import com.bakon.android.di.qualifiers.ApplicationContext;
import com.bakon.android.manager.ActManager;
import com.bakon.android.manager.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@Module(includes = {APIModule.class})
public final class ApplicationModule {

    private MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
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
    public MyApplication provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Resources provideResources() {
        return mApplication.getResources();
    }

    @Singleton
    @Provides
    public MMKVManager provideMMKVManager() {
        return new MMKVManager(mApplication);
    }

    @Singleton
    @Provides
    public RefWatcher provideRefWatcher() {
        if (LeakCanary.isInAnalyzerProcess(mApplication)) {
            return RefWatcher.DISABLED;
        }
        if (BuildConfig.DEBUG) {
            return LeakCanary.install(mApplication);
        }
        return RefWatcher.DISABLED;
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
    @Named(SPConfig.APP_SP_NAME)
    public SharePreferencesWrapper provideAPPSharePreferencesWrapper() {
        return new SharePreferencesWrapper(mApplication, SPConfig.APP_SP_NAME);
    }

    @Singleton
    @Provides
    public StorageOperator provideStorageOperator() {
        return new StorageOperator(mApplication);
    }


    @Singleton
    @Provides
    public HashMap<String, Object> provideHashMap() {
        return new HashMap();
    }

}
