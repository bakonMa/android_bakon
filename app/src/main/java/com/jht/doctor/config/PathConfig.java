package com.jht.doctor.config;


import com.jht.doctor.BuildConfig;

/**
 * @author: ZhaoYun
 * @date: 2017/11/3
 * @project: customer-android-2th
 * @detail:
 */
public interface PathConfig {


//    发现H5链接:
//    书籍http://dev.dr.jhtcm.com/books
//    百科http://hxd.dev.dr.jhtcm.com/baike






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
