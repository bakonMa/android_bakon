package com.junhetang.doctor.config;

import android.content.Context;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface SPConfig {
    String APP_SP_NAME = "sp_app";

    String SP_STR_TOKEN = "token";
    String SP_INT_SUTH_STATUS = "auth_status";
    String SP_INT_USER_TYPE = "user_type";
    String SP_SUTH_STATUS_FAIL_MSG = "auth_status_fail_msg";
    String SP_STR_PHONE = "phone";
    String SP_SERVICE_ACCID = "service_accid";
    String SP_NIM_ACCID = "accid";
    String SP_NIM_ACCTOKEN = "acctoken";

    String SP_USERBEAN = "userbean";
    //在线开方基础数据
    String SP_OPENPAPER_BASEDATA = "openpaper_basedata";
    //消息提醒状态
    String SP_MESSAGE_STATUS = "message_status";
    //红点-审核处方
    String SP_REDPOINT_EXT = "redpoint_ext";
    //红点-系统消息
    String SP_REDPOINT_SYS = "redpoint_sys";
    //红点-添加患者
    String SP_REDPOINT_FIR = "redpoint_fir";


    int GENERAL_SP_MODE = Context.MODE_PRIVATE;
    String LAST_ENTER_CODE = "last_enter_code";//最近的版本code
    String SP_KEY_BASE_CONFIG = "base_config";//科室、职称、擅长等信息

    String SP_BOOL_LASTCHECK_FORCEUPDATE_NAME = "isForceUpdate";//上一次检查更新时，是否是检查到要强制更新
    String SP_LONG_LASTCHECKUPDATE_TIME_NAME = "lastCheckUpdateTime";//上一次检查更新的时间戳

}
