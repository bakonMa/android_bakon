package com.renxin.doctor.activity.ui.bean_jht;


/**
 * SearchDrugBean 药材
 * Create at 2018/4/27 上午10:20 by mayakun 
 */
public class SearchDrugBean {


    /**
     * id : 380
     * name : 苍耳草
     * price : 0.01
     * unit : g
     * spec : g
     * use_flag : 1
     * datatype : 1
     */
    //搜索药材数据
    public int id;
    public String name;
    public double price;
    public String unit;
    public String spec;
    public int use_flag;
    //数据类型，1:药品，2：处方
    public int datatype;

}
