package com.jht.doctor.ui.bean;

/**
 * Created by mayakun on 2017/11/27
 * 其他小数据的bean
 */

public class OtherBean {
    //是否有交易密码
    public boolean tradePwdStatus;

    //充值验证 01:短信 02：交易密码
    public String rechargeValidate;
    //提现验证 01:短信 02：交易密码
    public String withdrawValidate;

}
