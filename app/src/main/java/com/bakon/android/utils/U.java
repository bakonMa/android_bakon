package com.bakon.android.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.SPConfig;
import com.bakon.android.ui.bean.OPenPaperBaseBean;
import com.bakon.android.ui.bean.UserBaseInfoBean;

/**
 * Create by mayakun at 2018/3/30 下午3:01
 */

public class U {


    //获取sp中 userbean 持久化
    public static void saveUserInfo(String jsonBean) {
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_USERBEAN, jsonBean);
    }

    //获取sp中 userbean 持久化
    public static UserBaseInfoBean getUserInfo() {
        String json = MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_USERBEAN, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, UserBaseInfoBean.class);
        }
    }

    //获取sp中 开方基础数据 持久化
    public static void saveOpenpaperBaseData(String jsonBean) {
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_OPENPAPER_BASEDATA, jsonBean);
    }

    //获取sp中 开方基础数据 持久化
    public static OPenPaperBaseBean getOpenpapeBaseData() {
        String json = MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_OPENPAPER_BASEDATA, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, OPenPaperBaseBean.class);
        }
    }

    //更新sp中本系统token
    public static void updateToken(String newToken) {
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, newToken);
    }

    //获取sp中本系统token
    public static String getToken() {
        return MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, "");
    }

    //获取sp中本系统phone
    public static String getPhone() {
        return MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_PHONE, "");
    }

    //获取sp中认证状态 是否是认证通过状态  0：未认证 1：审核中；2：审核通过 3：审核失败
    public static boolean isHasAuthOK() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_INT_SUTH_STATUS, 0) == 2;
    }

    //获取sp中认证状态 //0：未认证 1：审核中；2：审核通过 3：审核失败
    public static int getAuthStatus() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_INT_SUTH_STATUS, 0);
    }

    //获取sp中认证状态 认证状态 0：未认证 1：审核中；2：审核通过 3：审核失败
    public static void setAuthStatus(int status) {
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_SUTH_STATUS, status);
    }

    //获取sp中 用户类型 0:兼职，1:全职，2:第三方
    public static int getUserType() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_INT_USER_TYPE, 0);
    }

    //获取sp中 用户类型 0:兼职，1:全职，2:第三方
    public static void setUserType(int userType) {
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_USER_TYPE, userType);
    }

    //获取sp中认证状态失败 原因
    public static void setAuthStatusFailMsg(String failMsg) {
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_SUTH_STATUS_FAIL_MSG, failMsg);
    }

    //获取sp中认证状 文字 //0：未认证 1：审核中；2：审核通过 3：审核失败
    public static String getAuthStatusMsg() {
        String msg = "";
        switch (getAuthStatus()) {
            case 0:
                msg = MyApplication.getInstance().getString(R.string.str_autu_no);
                break;
            case 1:
                msg = MyApplication.getInstance().getString(R.string.str_auth_ing);
                break;
            case 2:
                msg = "认证成功";
                break;
            case 3:
                msg = MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_SUTH_STATUS_FAIL_MSG, "您的认证信息未通过，请重新认证！");
                break;
        }
        return msg;
    }

    //首页 审核处方 set红点
    public static void setRedPointExt(int status) {
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_REDPOINT_EXT, status);
    }

    //首页 审核处方 get红点
    public static int getRedPointExt() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_REDPOINT_EXT, 0);
    }

    //首页 系统消息 set红点
    public static void setRedPointSys(int status) {
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_REDPOINT_SYS, status);
    }

    //首页 系统消息 get红点
    public static int getRedPointSys() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_REDPOINT_SYS, 0);
    }//首页 添加患者 set红点

    public static void setRedPointFir(int status) {
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_REDPOINT_FIR, status);
    }

    //首页 添加患者 get红点
    public static int getRedPointFir() {
        return MyApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_REDPOINT_FIR, 0);
    }

    /**
     * token是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNoToken() {
        return TextUtils.isEmpty(MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN));
    }

    //获取sp中 消息提醒flag 根据phone为key
    public static Boolean getMessageStatus() {
        return MyApplication.getAppComponent().dataRepo().appSP().getBoolean(SPConfig.SP_MESSAGE_STATUS + getPhone(), true);
    }

    //获取sp中 设置消息提醒flag
    public static void setMessageStatus(boolean isCheck) {
        MyApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_MESSAGE_STATUS + getPhone(), isCheck);
    }


    //退出登录 清掉数据
    public static void logout() {
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_TOKEN, "");
        //nim 的accid，acctoken
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCID, "");
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_NIM_ACCTOKEN, "");
        //消息提醒
        MyApplication.getAppComponent().dataRepo().appSP().setBoolean(SPConfig.SP_MESSAGE_STATUS + getPhone(), true);
        //认证状态
        MyApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_SUTH_STATUS, 0);
        //用户信息
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_USERBEAN, "");
        //客服accid
        MyApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_SERVICE_ACCID, "");
        //审方 红点
        U.setRedPointExt(0);
        //系统消息 红点
        U.setRedPointSys(0);
        //患者添加 红点
        U.setRedPointFir(0);
    }

    //每页的size
    public static final int PAGE_SIZE = 15;
}
