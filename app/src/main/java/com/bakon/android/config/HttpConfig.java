package com.bakon.android.config;


import com.bakon.android.BuildConfig;

/**
 * @author: mayakun
 * @date: 2018/4/2
 */
public interface HttpConfig {

    //http私盐
    String SECRET_VALUE = "jht2018app";
    String SIGN_KEY = "sign";
    String TIMESTAMP = "timestamp";
    String BASE_URL = BuildConfig.BASE_URL;

    long CONNECT_TIMEOUT = 30 * 1000;//超时30s
    long IO_READ_TIMEOUT = 60 * 1000;
    long IO_WRITE_TIMEOUT = 60 * 1000;

    //Umeng分享AppId
    String UMENG_APPKEY = "5aded051f29d985b64000026";

    String WX_APP_ID = "wx7f3bde58fbc30588";
    String WX_APP_SECRET = "8808364972a4b28e751598584fb1b9e0";
    String QQ_APP_ID = "1106783321";
    String QQ_APP_ID_Key = "j5tRZ5zNzbhdoNQd";
//    String QQ_APP_ID = "1104231476";
//    String QQ_APP_ID_Key = "WJXfjBPBJpTJWfr5";

    String HTTP_HEADER_OS = "android";
    String HTTP_HEADER_CONTENTTYPE_KEY = "Content-Type";
    String HTTP_HEADER_CONTENTTYPE_VALUE = "application/json; charset=UTF-8";
    String HTTP_HEADER_DOWNLOAD_APK = "DownloadApk";

}
