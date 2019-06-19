package com.bakon.android.config;

/**
 * Created by table on 2017/11/28.
 * description: eventbus事件
 */

public interface EventConfig {
    int REFRESH_APK_DOWNLOAD_PROGRESS = 0x110; //刷新Apk安装包的下载进度

    int EVENT_KEY_AUTH_STATUS = 0x111;//刷新个人认证状态

    int EVENT_KEY_H5_BOOKS_SHARE = 0x116;//书籍 分享
    int EVENT_KEY_USERINFO = 0x118;//分享个人资料
    //home tab 红点
    int EVENT_KEY_REDPOINT_HOME = 0x122;//首页 工作室 红点
    int EVENT_KEY_REDPOINT_HOME_CHECK = 0x123;//首页 工作室 审核处方红点
    int EVENT_KEY_REDPOINT_HOME_SYSMSG = 0x124;//首页 工作室 系统消息红点
    int EVENT_KEY_REDPOINT_PATIENT = 0x125;//首页 患者 红点

    int EVENT_KEY_NIM_LOGIN = 0x127;//云信im登录成功
    int EVENT_KEY_XG_BINDTOKEN = 0x129;//后台绑定信鸽token

    int EVENT_KEY_REMARKNAME = 0x130;//修改备注
    int EVENT_KEY_CLOSE_CHAT= 0x131;//结束咨询
    int EVENT_KEY_BASEDATA_NULL= 0x136;//基础数据空异常

    int EVENT_KEY_ADD_PATIENT= 0x137;//添加患者成功

}
