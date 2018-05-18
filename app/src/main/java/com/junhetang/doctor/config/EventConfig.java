package com.junhetang.doctor.config;

/**
 * Created by table on 2017/11/28.
 * description: eventbus事件
 */

public interface EventConfig {
    int REFRESH_APK_DOWNLOAD_PROGRESS = 0x121; //刷新Apk安装包的下载进度


    //*********************jht**********************
    int EVENT_KEY_AUTH_STATUS = 0x111;//刷新个人认证状态
    int EVENT_KEY_ADDBANKCARD_OK = 0x112;//添加银行卡
    int EVENT_KEY_DELBANKCARD_OK = 0x113;//删除银行卡
    int EVENT_KEY_WITHRAW_OK = 0x114;//提现成功
    int EVENT_KEY_CHAT_SELECT_COMMMSG = 0x115;//聊天，选择发送常用语
    int EVENT_KEY_H5_BOOKS_SHARE = 0x116;//书籍 分享
    int EVENT_KEY_CHOOSE_PATIENT = 0x117;//开方 选择患者
    int EVENT_KEY_USERINFO = 0x118;//分享个人资料
    int EVENT_KEY_CHECKPAPER_OK = 0x119;//审核处方提交成功

    int EVENT_KEY_UPDATE_NOTICE = 0x120;//公告修改成功
    int EVENT_KEY_UPDATE_EXPLAIN = 0x121;//简介修改成功
    //home tab 红点
    int EVENT_KEY_REDPOINT_HOME = 0x122;//首页 工作室 红点
    int EVENT_KEY_REDPOINT_HOME_CHECK = 0x123;//首页 工作室 审核处方红点
    int EVENT_KEY_REDPOINT_HOME_SYSMSG = 0x124;//首页 工作室 系统消息红点
    int EVENT_KEY_REDPOINT_PATIENT = 0x125;//首页 患者 红点

    int EVENT_KEY_SERVICE_MESSAGE = 0x126;//客服消息
    int EVENT_KEY_NIM_LOGIN = 0x127;//云信im登录成功
    int EVENT_KEY_NIM_LOGOUT = 0x128;//云信im被踢，进入登录画面

    int EVENT_KEY_XG_BINDTOKEN = 0x129;//后台绑定信鸽token

    int EVENT_KEY_REMARKNAME = 0x130;//修改备注
    int EVENT_KEY_CLOSE_CHAT= 0x131;//结束咨询

}
