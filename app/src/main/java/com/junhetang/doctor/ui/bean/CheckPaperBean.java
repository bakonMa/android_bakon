package com.junhetang.doctor.ui.bean;


/**
 * CheckPaperBean 审核处方
 * Create at 2018/5/4 下午4:51 by mayakun
 */
public class CheckPaperBean {


    /**
     * id : 98
     * patient_name : 开心果
     * sex : 1
     * age : 12
     * phone : 12357122365
     * create_time :
     */

    public int id;
    public String patient_name;
    public int sex;//男：0、女：1
    public String age;
    public String phone;
    public String create_time;
    public String status_name;//支付状态
    public int presc_type;//1、手工开方；2、拍照开方

}
