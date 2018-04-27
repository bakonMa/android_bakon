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

    //微信分享AppId
//    String UMENG_APPKEY = "5aced483b27b0a303b000044";
    String UMENG_APPKEY = "5aded051f29d985b64000026";
    String WX_APP_ID = "wxa792384772439d0f";
    String WX_APP_SECRET = "2e37c33fd6ff031ebeae1cbeb94e6219";
//    String QQ_APP_ID = "1103454520";
//    String QQ_APP_ID_Key = "IT6H5qigvERIKyzg";
    String QQ_APP_ID = "1104231476";
    String QQ_APP_ID_Key = "WJXfjBPBJpTJWfr5";

    String HTTP_HEADER_OS = "android";
    String HTTP_HEADER_CONTENTTYPE_KEY = "Content-Type";
    String HTTP_HEADER_CONTENTTYPE_VALUE = "application/json";
    String HTTP_HEADER_TOKEN_KEY = "Token";
    String HTTP_HEADER_DOWNLOAD_APK = "DownloadApk";

}
