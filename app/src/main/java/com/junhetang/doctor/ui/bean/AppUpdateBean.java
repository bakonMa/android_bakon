package com.junhetang.doctor.ui.bean;

/**
 * AppUpdateBean APP 版本检测
 * Create at 2018/5/14 上午9:58 by mayakun
 */
public class AppUpdateBean {


    /**
     * name : test1.3.1
     * version_code : 4
     * current_version : 1.30
     * comments : 更新说明1、2、3
     * down_url : https://jhtcm.com/1525490623.apk
     * isforced : 0
     * md5code : dfe50010a2519c2657bc3902d3029046
     */

    public String name;
    public int version_code;
    public String current_version;
    public String comments;
    public String down_url;
    public int isforced;//是否强制更新；1：强制；0不强制
    public String md5code;
}