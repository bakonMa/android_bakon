package com.renxin.doctor.activity.ui.bean_jht;


/**
 * DrugBean 添加的药材
 * Create at 2018/4/27 上午10:20 by mayakun
 */
public class DrugBean{
    //添加药材使用
    public int drug_id;
    public String name;
    public String unit;//单位
    public int drug_num;//用量
    public double price;//价格
    public String decoction;//用法（常规）
    public int use_flag;//是否可用 1：能使用；0：不能使用

}
