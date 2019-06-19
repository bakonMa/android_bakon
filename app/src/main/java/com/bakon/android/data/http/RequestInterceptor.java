package com.bakon.android.data.http;

import android.os.Build;

import com.bakon.android.BuildConfig;
import com.bakon.android.config.HttpConfig;
import com.bakon.android.utils.U;

import java.io.IOException;
import java.net.ConnectException;

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
        boolean connected = true;
        //TODO
//                NetworkUtil.isNetworkConnected(MyApplication.getInstance());
        if (connected) {
            request = chain.request()
                    .newBuilder()
                    .header(HttpConfig.HTTP_HEADER_CONTENTTYPE_KEY, HttpConfig.HTTP_HEADER_CONTENTTYPE_VALUE)
                    .header("OS", HttpConfig.HTTP_HEADER_OS)
                    .header("APPVERSION", BuildConfig.VERSION_NAME)
                    .header("APPVERSION-CODE", BuildConfig.VERSION_CODE + "")
                    .header("MODEL", Build.MODEL)//手机型号
                    .header("RELEASE", Build.VERSION.RELEASE)//系统版本号
                    .header("TOKEN", U.getToken())//token
                    .build();
            return chain.proceed(request);

        } else {
            throw new ConnectException();
        }
    }
}
