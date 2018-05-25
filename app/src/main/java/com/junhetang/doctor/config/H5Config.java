package com.junhetang.doctor.config;


import com.junhetang.doctor.BuildConfig;

/**
 * H5Config
 * Create at 2018/5/22 下午3:58 by mayakun 
 */
public interface H5Config {
    String CRASH_DIR_NAME = "crash";
    String H5_SHARE_DOWNLOAD = BuildConfig.BASE_URL + "download.html";
    
    //个人名片
    String H5_USERCARD = BuildConfig.BASE_URL + "web_mycard/";
    String H5_USERCARD_TITLE = "个人名片";
    //个人资料 预览
    String H5_USERINFO = BuildConfig.BASE_URL + "web_userinfo/";
    String H5_USERINFO_TITLE = "个人信息预览";
    //问诊单
    String H5_ASKPAPER = BuildConfig.BASE_URL + "web_edit_inquiry/";
    String H5_ASKPAPER_INFO = BuildConfig.BASE_URL + "web_inquiryinfo?id=";
    //随诊单
    String H5_FOLLOWPAPER = BuildConfig.BASE_URL + "web_checkups/";
    String H5_FOLLOWPAPER_INFO = BuildConfig.BASE_URL + "web_checkupsinfo?id=";
    //发现 书籍 百科
    String H5_BOOKS = BuildConfig.BASE_URL + "books/";
    String H5_BAIKE = BuildConfig.BASE_URL + "baike/";
    //关于 功能介绍
    String H5_FUNCTION_INTRODUCTION = BuildConfig.BASE_URL + "static/Introduction.html";
    String H5_FUNCTION_INTRODUCTION_TITLE = "功能介绍";
    //产品说明
    String H5_PRODUCE_INFO = BuildConfig.BASE_URL + "static/product-state.html";
    String H5_PRODUCE_INFO_TITLE = "产品说明";
    //用户协议
    String H5_AGREEMENT = BuildConfig.BASE_URL + "static/agreement.html";
    String H5_AGREEMENT_TITLE = "用户协议";
    //审核处方
    String H5_CHECKPAPER = BuildConfig.BASE_URL + "check_extraction?id=";
    //处方详情
    String H5_PAPER_DETAIL = BuildConfig.BASE_URL + "extraction_info?id=";
    //系统消息
    String H5_SYSTEM_MSG = BuildConfig.BASE_URL + "msgInfo?id=";


}
