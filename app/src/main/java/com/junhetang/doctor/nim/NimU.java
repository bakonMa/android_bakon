package com.junhetang.doctor.nim;

import android.text.TextUtils;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.SPConfig;

/**
 * 网易云im 业务工具
 * NimU
 * Create by mayakun at 2018/3/30 下午3:01
 */


public class NimU {

    //获取sp中account
    public static String getNimAccount() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_NIM_ACCID, "");
    }

    //set sp中account
    public static void setNimAccount(String account) {
         DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCID, account);
    }

    //获取sp中token
    public static String getNimToken() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_NIM_ACCTOKEN, "");
    }

    //set sp中account
    public static void setNimToken(String token) {
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCTOKEN, token);
    }

    /**
     * token是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNimNoToken() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_NIM_ACCTOKEN));
    }

    /**
     * account是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNimNoAccount() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_NIM_ACCID));
    }

}
