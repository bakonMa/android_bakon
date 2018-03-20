package com.jht.doctor.data.api.http;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static com.jht.doctor.config.HttpConfig.HTTP_HEADER_DOWNLOAD_APK;

/**
 * Created by zhaoyun on 2018/1/12.
 */
public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        Response.Builder responseBuilder = originalResponse.newBuilder();

        String apkDownloadIdentifier = originalResponse.request().header(HTTP_HEADER_DOWNLOAD_APK);

        //该请求是否带有下载Apk的标签
        if (!TextUtils.isEmpty(apkDownloadIdentifier)) {
            DownloadProgressResponseBody downloadResponseBody = new DownloadProgressResponseBody(0 , apkDownloadIdentifier, originalResponse.body());
            responseBuilder.body(downloadResponseBody);
        }

        return responseBuilder.build();
    }

}
