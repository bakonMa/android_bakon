package com.bakon.android.di.components;

import android.content.Context;
import android.content.res.Resources;

import com.bakon.android.application.MyApplication;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.http.DataRepository;
import com.bakon.android.data.http.HttpAPIWrapper;
import com.bakon.android.data.localdata.MMKVManager;
import com.bakon.android.data.localdata.SharePreferencesWrapper;
import com.bakon.android.di.modules.APIModule;
import com.bakon.android.di.modules.ApplicationModule;
import com.bakon.android.di.qualifiers.ApplicationContext;
import com.bakon.android.manager.ActManager;
import com.bakon.android.manager.ManagerRepository;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                APIModule.class
        }
)
public interface ApplicationComponent {

    @ApplicationContext
    Context appContext();

    MyApplication application();

    Resources resources();

    MMKVManager mmkvManager();

    RefWatcher refWatcher();

    ActManager actManager();

    @Named(SPConfig.APP_SP_NAME)
    SharePreferencesWrapper appSharePreferencesWrapper();

    //网络请求api
    HttpAPIWrapper httpAPIWrapper();

    ManagerRepository mgrRepo();

    DataRepository dataRepo();

    HashMap<String, Object> sessionMap();

    void inject(MyApplication myApplication);

}
