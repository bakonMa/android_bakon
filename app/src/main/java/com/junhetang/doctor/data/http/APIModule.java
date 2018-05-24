package com.junhetang.doctor.data.http;

import com.google.gson.Gson;
import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.localdata.SharePreferencesWrapper;
import com.junhetang.doctor.data.localdata.StorageOperator;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * APIModule
 * Create at 2018/5/22 下午4:00 by mayakun
 */
@Module
public final class APIModule {

    private final DocApplication mApplication;

    public APIModule(DocApplication application) {
        mApplication = application;
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
    public HttpAPIWrapper provideHttpAPIWrapper(HttpAPI httpAPI) {
        return new HttpAPIWrapper(httpAPI);
    }

    @Provides
    @Singleton
    public RequestInterceptor provideRequestInterceptor() {
        return new RequestInterceptor();
    }

    @Provides
    @Singleton
    public ResponseInterceptor provideResponseInterceptor() {
        return new ResponseInterceptor();
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(RequestInterceptor requestInterceptor, ResponseInterceptor responseInterceptor) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        builder.connectTimeout(HttpConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HttpConfig.IO_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConfig.IO_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(requestInterceptor)
                .addNetworkInterceptor(responseInterceptor)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
//                .sslSocketFactory(DefaultSSLSocketFactory.getSSLSocketFactory(CustomerApplication.getAppInstance().mContext,"BKS",null))
        ;
        return builder.build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(HttpConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder.build();
    }

    @Singleton
    @Provides
    public HttpAPI provideHttpAPI(Retrofit restAdapter) {
        return restAdapter.create(HttpAPI.class);
    }

}
