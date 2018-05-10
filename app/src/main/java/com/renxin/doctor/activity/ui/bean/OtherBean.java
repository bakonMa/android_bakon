package com.renxin.doctor.activity.ui.bean;

/**
 * Created by mayakun on 2017/11/27
 * 其他小数据的bean
 */

public class OtherBean {
    //认证状态 0：未认证 1：审核中；2：审核通过 3：审核失败
    public int status;
    //accid 获取memb_no 使用
    public String memb_no;
    //更新token使用
    public String token;

    //首页红点 使用
    public int ext_status;//审核开方红点 1:有 0：无
    public int fri_status;//新患者好友红点 1:有 0：无
    public int sys_status;//系统消息红点 1:有 0：无


}
