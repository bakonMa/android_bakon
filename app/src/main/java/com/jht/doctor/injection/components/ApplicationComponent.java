package com.jht.doctor.injection.components;

import android.content.Context;
import android.content.res.Resources;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.http.APIModule;
import com.jht.doctor.data.http.DataRepository;
import com.jht.doctor.injection.modules.ApplicationModule;
import com.jht.doctor.injection.qualifiers.ApplicationContext;
import com.jht.doctor.manager.ManagerRepository;
import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;

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

    DocApplication application();

    Resources resources();

    RefWatcher refWatcher();

    ManagerRepository mgrRepo();

    DataRepository dataRepo();

    HashMap<String, Object> sessionMap();

    void inject(DocApplication docApplication);

}
