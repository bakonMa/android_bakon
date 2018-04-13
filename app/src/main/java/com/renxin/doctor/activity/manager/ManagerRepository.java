package com.renxin.doctor.activity.manager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
@Singleton
public final class ManagerRepository {

    private final ActManager mActManager;
    private final CrashHandler mCrashHandler;

    @Inject
    public ManagerRepository( ActManager actManager , CrashHandler crashHandler){
        mActManager = actManager;
        mCrashHandler = crashHandler;
    }


    public ActManager actMgr(){
        return mActManager;
    }

    public CrashHandler crashHandler(){
        return mCrashHandler;
    }

}
