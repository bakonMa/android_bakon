package com.jht.doctor.config;

/**
 * Created by table on 2017/11/28.
 * description: eventbus事件
 */

public interface EventConfig {
    int REFRESH_PERSONAL = 0x111;   //刷新个人中心数据

    int REFRESH_APPLY_INFO_BASIC = 0x1121;  //刷新基本信息页面

    int REFRESH_APPLY_INFO_JOB = 0x1122;    //刷新工作信息页面

    int REQUEST_HOMELOAN = 0x113;   //未登录状态下点击申请，登录成功后对借款首页逻辑处理

    int REFRESH_MAX_AMT = 0x114;    //刷新借款主页最大额度接口

    int WHERE_TO_GO = 0x115;    //未登录状态下 从个人中心登录成功后对借款主页的逻辑处理

    int FINISH_LOAN = 0x116;    //完成借款申请三步骤后的通知

    int RECHARGE_OK = 0x117;    //充值成功后对前置页面的关闭
    int WITHDRAW_OK = 0x119;    //提现成功后对前置页面的关闭

    int REFRESH_BANKLIST = 0x118;   //解绑成功后对银行卡列表的刷新

    int CONTROL_FRAGMENT = 0x119;    //控制fragment

    int REFRESH_ORDER = 0x120;  //刷新订单列表

    int REFRESH_APK_DOWNLOAD_PROGRESS = 0x121; //刷新Apk安装包的下载进度
    int REFRESH_MESSAGE_RED_POINT = 0x122; //取消【我】消息红点
}
