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
    int EVENT_KEY_USERINFO= 0x118;//分享个人资料
}
