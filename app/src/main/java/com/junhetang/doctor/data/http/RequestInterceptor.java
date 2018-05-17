package com.junhetang.doctor.data.http;

import android.os.Build;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.utils.U;

import java.io.IOException;

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
//        if (chain.request().url().url().toString().contains("clientVersion/search")) {
//            request = chain.withConnectTimeout(3, TimeUnit.SECONDS).request();//检查更新接口只设置3秒的超时
//        } else {
            request = chain.request()
                    .newBuilder()
                    .header(HttpConfig.HTTP_HEADER_CONTENTTYPE_KEY, HttpConfig.HTTP_HEADER_CONTENTTYPE_VALUE)
                    .header("OS", HttpConfig.HTTP_HEADER_OS)
                    .header("APPVERSION", BuildConfig.VERSION_NAME)
                    .header("APPVERSION_CODE", BuildConfig.VERSION_CODE + "")
                    .header("MODEL", Build.MODEL)//手机型号
                    .header("RELEASE", Build.VERSION.RELEASE)//系统版本号
                    .header("TOKEN", U.getToken())//token
                    .build();
//        }
        return chain.proceed(request);
    }
}