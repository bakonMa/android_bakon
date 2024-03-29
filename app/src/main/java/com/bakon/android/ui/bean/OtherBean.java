package com.bakon.android.ui.bean;

/**
 * Created by mayakun on 2017/11/27
 * 其他小数据的bean
 */

public class OtherBean {
    //认证状态 0：未认证 1：审核中；2：审核通过 3：审核失败
    public int status;
    public String fail_msg;//未认证 文言（审核通过 空）
    public int user_type;//用户类型，0:兼职，1:全职，2:第三方

    //accid 获取memb_no 使用
    public String memb_no;
    //更新token使用
    public String token;

    //首页红点 使用
    public int ext_status;//审核开方红点 1:有 0：无
    public int fri_status;//新患者好友红点 1:有 0：无
    public int sys_status;//系统消息红点 1:有 0：无


    public int rcode;//添加患者 使用 1：成功 -2：该患者已经存在 -3：手机号格式不正确 -4：请填写完整参数 -5：请输入正常年龄
    public String msg;//添加患者 msg



}
