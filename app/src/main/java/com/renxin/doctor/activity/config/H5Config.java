package com.renxin.doctor.activity.config;


import com.renxin.doctor.activity.BuildConfig;

/**
 * @author: ZhaoYun
 * @date: 2017/11/3
 * @project: customer-android-2th
 * @detail:
 */
public interface H5Config {


    //    发现H5链接:
//    书籍http://dev.dr.jhtcm.com/books
//    百科http://hxd.dev.dr.jhtcm.com/baike
    //个人名片
    String H5_USERINFO = BuildConfig.BASE_URL + "web_mycard/";
    String H5_USERINFO_TITLE = "个人名片";
    //问诊单
    String H5_ASKPAPER = BuildConfig.BASE_URL + "web_edit_inquiry/";
    String H5_ASKPAPER_INFO = BuildConfig.BASE_URL + "web_inquiryinfo?id=";
    //随诊单
    String H5_FOLLOWPAPER = BuildConfig.BASE_URL + "web_checkups/";
    String H5_FOLLOWPAPER_INFO = BuildConfig.BASE_URL + "web_checkupsinfo?id=";
    //发现 书籍 百科
    String H5_BOOKS = BuildConfig.BASE_URL + "books/";
    String H5_BAIKE = BuildConfig.BASE_URL + "baike/";

    //*******************jht end**********************


    String CRASH_DIR_NAME = "crash";
    //    String DUBUG_H5_BASE = "http://116.62.244.195:9999/";//一期测试环境 h5地址
    String DUBUG_H5_BASE = "http://10.255.233.220:9999/";//二期测试环境 h5地址

    String RLEASE_H5_BASE = "http://front.sd-bao.com/";//一期正式环境 h5地址
    //1、常见问题
    String H5_QUESTION = (BuildConfig.DEBUG ? DUBUG_H5_BASE : RLEASE_H5_BASE) + "#/app/commonquery";
    // 2、关于我们
    String H5_ABOUTUS = (BuildConfig.DEBUG ? DUBUG_H5_BASE : RLEASE_H5_BASE) + "#/app/AboutUs";
    // 3、借款人服务协议
    String H5_BORROWERPROTOCOL = (BuildConfig.DEBUG ? DUBUG_H5_BASE : RLEASE_H5_BASE) + "#/app/BorrowerProtocol";
    // 4、注册服务协议
    String H5_REGISTERPROTOCOL = (BuildConfig.DEBUG ? DUBUG_H5_BASE : RLEASE_H5_BASE) + "#/app/RegisterProtocol";

}