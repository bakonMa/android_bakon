package com.renxin.doctor.activity.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.SPConfig;
import com.renxin.doctor.activity.ui.bean.ConfigBean;
import com.renxin.doctor.activity.ui.bean_jht.UserBaseInfoBean;

/**
 * Create by mayakun at 2018/3/30 下午3:01
 */

public class U {


    //获取sp中 userbean 持久化
    public static void saveUserInfo(String jsonBean) {
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_USERBEAN, jsonBean);
    }

    //获取sp中 userbean 持久化
    public static UserBaseInfoBean getUserInfo() {
        String json = DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_USERBEAN, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, UserBaseInfoBean.class);
        }
    }


    //获取sp中本系统token
    public static String getToken() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, "");
    }

    //获取sp中认证状态
    public static int getAuthStatus() {
        return DocApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_INT_SUTH_STATUS, 0);
    }

    //获取sp中认证状态 认证状态 0：未认证 1：审核中；2：审核通过 3：审核失败
    public static void setAuthStatus(int status) {
        DocApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_SUTH_STATUS, status);
    }

    /**
     * token是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNoToken() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN));
    }

    //获取sp中 消息提醒flag
    public static Boolean getMessageStatus() {
        return DocApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_MESSAGE_STATUS, true);
    }

    //获取sp中 设置消息提醒flag
    public static void setMessageStatus(boolean isCheck) {
        DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_MESSAGE_STATUS, isCheck);
    }


    //获取sp中 设置消息提醒flag
    public static void logout() {
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, "");
        //nim 的accid，acctoken
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCID, "");
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCTOKEN, "");
        //消息提醒
        DocApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_MESSAGE_STATUS, true);
        //认证状态
        DocApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_SUTH_STATUS, 0);
        //用户信息
        DocApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_INT_SUTH_STATUS, "");
    }

    //每页的size
    public static final int PAGE_SIZE = 15;


//    //交易状态
//    public final static HashMap<String, String> DEAL_STATUS = new HashMap<>();
//
//    static {
//        DEAL_STATUS.put("CONFIRMING", "在途中");
//        DEAL_STATUS.put("SUCCESS", "交易成功");
//        DEAL_STATUS.put("FAIL", "交易失败");
//    }

    //获取base信息 ConfigBean
    public static ConfigBean getConfigData() {
        String json = DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_KEY_BASE_CONFIG);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, ConfigBean.class);
        }
    }


}
