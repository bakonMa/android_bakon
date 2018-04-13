package com.renxin.doctor.activity.config;


import com.renxin.doctor.activity.BuildConfig;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface HttpConfig {

    //私盐
    String SECRET_VALUE = "jht2018app";
    String SIGN_KEY = "sign";
    String TIMESTAMP = "timestamp";
    String BASE_URL = BuildConfig.BASE_URL;

    long CONNECT_TIMEOUT = 30 * 1000;//超时30s
    long IO_READ_TIMEOUT = 60 * 1000;
    long IO_WRITE_TIMEOUT = 60 * 1000;

    String HTTP_HEADER_OS = "android";
    String HTTP_HEADER_CONTENTTYPE_KEY = "Content-Type";
    String HTTP_HEADER_CONTENTTYPE_VALUE = "application/json";
    String HTTP_HEADER_TOKEN_KEY = "Token";
    String HTTP_HEADER_DOWNLOAD_APK = "DownloadApk";

}
