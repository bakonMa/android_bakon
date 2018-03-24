package com.jht.doctor.config;


import com.jht.doctor.BuildConfig;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface HttpConfig {

    String BASE_URL = BuildConfig.BASE_URL;
    String NOLOGIN_CODE = "1001";//1001	未登录
    String SIGN_ERROR_CODE = "1002";//1002	签名错误

    long CONNECT_TIMEOUT = 30 * 1000;//超时30s
    long IO_READ_TIMEOUT = 60 * 1000;
    long IO_WRITE_TIMEOUT = 60 * 1000;

    String HTTP_HEADER_OS = "android";
    String HTTP_HEADER_CONTENTTYPE_KEY = "Content-Type";
    String HTTP_HEADER_CONTENTTYPE_VALUE = "application/json";
    String HTTP_HEADER_TOKEN_KEY = "Token";
    String HTTP_HEADER_DOWNLOAD_APK = "DownloadApk";

}
