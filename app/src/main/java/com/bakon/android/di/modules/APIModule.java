package com.bakon.android.di.modules;

import com.bakon.android.BuildConfig;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.data.http.HttpAPI;
import com.bakon.android.data.http.HttpAPIWrapper;
import com.bakon.android.data.http.RequestInterceptor;
import com.bakon.android.data.http.ResponseInterceptor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * APIModule
 * Create at 2018/5/22 下午4:00 by mayakun
 */
@Module
public final class APIModule {

    @Singleton
    @Provides
    public HttpAPIWrapper provideHttpAPIWrapper(HttpAPI httpAPI) {
        return new HttpAPIWrapper(httpAPI);
    }

    @Singleton
    @Provides
    public HttpAPI provideHttpAPI(Retrofit retrofit) {
        return retrofit.create(HttpAPI.class);
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(HttpConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
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

}
