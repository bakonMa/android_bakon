package com.bakon.android.data.http;

import com.bakon.android.config.SPConfig;
import com.bakon.android.data.localdata.SharePreferencesWrapper;
import com.bakon.android.data.localdata.StorageOperator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
@Singleton
public final class DataRepository {

    private final HttpAPIWrapper mHttpAPIWrapper;
    private final SharePreferencesWrapper mAppSharePreferencesWrapper;
    private final StorageOperator mStorageOperator;

    @Inject

    public DataRepository
            (
                    HttpAPIWrapper httpAPIWrapper,
                    @Named(SPConfig.APP_SP_NAME) SharePreferencesWrapper appSharePreferencesWrapper,
                    StorageOperator storageOperator
            ) {
        mHttpAPIWrapper = httpAPIWrapper;
        mAppSharePreferencesWrapper = appSharePreferencesWrapper;
        mStorageOperator = storageOperator;
    }

    public HttpAPIWrapper http() {
        return mHttpAPIWrapper;
    }

    public SharePreferencesWrapper appSP() {
        return mAppSharePreferencesWrapper;
    }

    public StorageOperator storage() {
        return mStorageOperator;
    }

}
