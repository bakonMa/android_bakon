package com.renxin.doctor.activity.injection.components;

import android.content.Context;
import android.content.res.Resources;

import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.http.APIModule;
import com.renxin.doctor.activity.data.http.DataRepository;
import com.renxin.doctor.activity.injection.modules.ApplicationModule;
import com.renxin.doctor.activity.injection.qualifiers.ApplicationContext;
import com.renxin.doctor.activity.manager.ManagerRepository;
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
