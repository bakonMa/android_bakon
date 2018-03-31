package com.jht.doctor.nim;

import android.text.TextUtils;

import com.jht.doctor.application.DocApplication;

/**
 * 网易云im 业务工具
 * NimU
 * Create by mayakun at 2018/3/30 下午3:01
 */


public class NimU {

    private static final String KEY_NIM_ACCOUNT = "nim_account";
    private static final String KEY_NIM_TOKEN = "nim_token";

    //获取sp中account
    public static String getNimAccount() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(KEY_NIM_ACCOUNT, "");
    }

    //set sp中account
    public static void setNimAccount(String account) {
         DocApplication.getAppComponent().dataRepo().appSP().setString(KEY_NIM_ACCOUNT, account);
    }

    //set sp中account
    public static void setNimToken(String token) {
        DocApplication.getAppComponent().dataRepo().appSP().setString(KEY_NIM_TOKEN, token);
    }


    /**
     * account是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNimNoAccount() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(KEY_NIM_ACCOUNT));
    }

    //获取sp中token
    public static String getNimToken() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(KEY_NIM_TOKEN, "");
    }

    /**
     * token是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNimNoToken() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(KEY_NIM_TOKEN));
    }


}
