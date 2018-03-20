package com.jht.doctor.data.api.http;

import com.jht.doctor.config.HttpConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.application.CustomerApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by table on 2017/11/22.
 * description:
 */

public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;
        if (chain.request().url().url().toString().contains("clientVersion/search")) {
            request = chain.withConnectTimeout(3, TimeUnit.SECONDS).request();//检查更新接口只设置3秒的超时
        } else {
            request = chain.request()
                    .newBuilder()
                    .header(HttpConfig.HTTP_HEADER_CONTENTTYPE_KEY, HttpConfig.HTTP_HEADER_CONTENTTYPE_VALUE)
                    .header(HttpConfig.HTTP_HEADER_TOKEN_KEY, CustomerApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))
                    .build();
        }
        return chain.proceed(request);
    }
}
