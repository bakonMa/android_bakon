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

    long CONNECT_TIMEOUT = 10 * 1000;

    long IO_READ_TIMEOUT = 60 * 1000;
    long IO_WRITE_TIMEOUT = 60 * 1000;

    String HTTP_HEADER_CONTENTTYPE_KEY = "Content-Type";
    String HTTP_HEADER_CONTENTTYPE_VALUE = "application/json";
    String HTTP_HEADER_TOKEN_KEY = "Token";
    String HTTP_HEADER_DOWNLOAD_APK = "DownloadApk";

    enum HttpErrorCode{
        OVERDUE("TOKEN_NOT_EXIST");

        HttpErrorCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        private String code;
    }

}
