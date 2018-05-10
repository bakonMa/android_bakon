package com.renxin.doctor.activity.config;

/**
 * Created by table on 2017/11/28.
 * description: eventbus事件
 */

public interface EventConfig {
    int REFRESH_PERSONAL = 0x111;//刷新个人中心数据
    int REQUEST_HOMELOAN = 0x113;//未登录状态下点击申请，登录成功后对借款首页逻辑处理
    int REFRESH_MAX_AMT = 0x114; //刷新借款主页最大额度接口
    int REFRESH_ORDER = 0x120;//刷新订单列表
    int REFRESH_APK_DOWNLOAD_PROGRESS = 0x121; //刷新Apk安装包的下载进度
    int REFRESH_MESSAGE_RED_POINT = 0x122; //取消【我】消息红点


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
}
