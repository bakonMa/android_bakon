package com.renxin.doctor.activity.ui.bean_jht;


/**
 * CommPaperInfoBean 常用方详情
 * Create at 2018/4/28 上午10:44 by mayakun 
 */
public class CommPaperInfoBean {

    /**
     * name	string	药品名称
     drug_num	int	用量
     decoction	string	煎法
     unit	string	单位
     spec	string	规格
     price	string	单价
     use_flag	int	1：能使用；0：不能使用
     */

    public int id;
    public String name;
    public int drug_num;
    public String decoction;
    public String unit;
    public String spec;
    public double price;
    public int use_flag;
}
