package com.bakon.android.ui.bean;

/**
 * DealDetailBean  交易明细
 * Create at 2018/4/11 下午6:52 by mayakun
 */

public class DealDetailBean {


    /**
     * "money": "5",
     * "deal_time": "2018-05-09",
     * "type": "提现"
     */

    public String type;
    public int type_id;//-1：提现 0：咨询 1：开方提成
    public int status;//提现状态：0:待受理 1：受理中 2：提现成功 -1:拒绝受理
    public String money;
    public String deal_time;
    public String patient_name;//就诊人
}
