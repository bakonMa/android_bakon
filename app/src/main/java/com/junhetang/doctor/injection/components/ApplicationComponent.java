package com.junhetang.doctor.injection.components;

import android.content.Context;
import android.content.res.Resources;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.APIModule;
import com.junhetang.doctor.data.http.DataRepository;
import com.junhetang.doctor.injection.modules.ApplicationModule;
import com.junhetang.doctor.injection.qualifiers.ApplicationContext;
import com.junhetang.doctor.manager.ManagerRepository;
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
