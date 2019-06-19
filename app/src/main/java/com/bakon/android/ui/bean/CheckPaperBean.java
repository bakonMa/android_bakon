package com.bakon.android.ui.bean;


/**
 * CheckPaperBean 审核处方
 * Create at 2018/5/4 下午4:51 by mayakun
 */
public class CheckPaperBean {

    public int id;
    public String patient_name;
    public int sex;//男：0、女：1
    public String age;
    public String phone;
    public String create_time;
    public String status_name;//支付状态
    public int presc_type;//1、手工开方；2、拍照开方
    public int z_status;//0：未转方 1：已转方

    //健康档案使用
    public String bz_remark;//主诉及辩证
    public String service_price;//诊疗费
    public String total_drug;//药品费
}
