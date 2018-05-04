package com.renxin.doctor.activity.utils;

/**
 * Constant 常量
 * Create at 2018/4/9 下午4:43 by mayakun
 */
public interface Constant {
    //上传图片 0：头像 1：其他认证图片 2:拍照开方照片
    String UPLOADIMG_TYPE_0 = "0";
    String UPLOADIMG_TYPE_1 = "1";
    String UPLOADIMG_TYPE_2 = "2";

    //下载链接（可以使用应用宝的推广链接）
    String APP_SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.renxin.doctor.activity";

    //就诊人与患者的关系
    String[] RELATION_TYPE = {"本人", "父母", "子女", "其他亲属", "其他"};

    //发送自定义消息记录 1:问诊单 2:随诊单 3:开方
    int CHAT_RECORD_TYPE_1 = 1;
    int CHAT_RECORD_TYPE_2 = 2;
    int CHAT_RECORD_TYPE_3 = 3;


}
