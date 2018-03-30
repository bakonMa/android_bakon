package com.jht.doctor.nim;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.ui.bean.ConfigBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
