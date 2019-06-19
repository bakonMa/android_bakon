package com.bakon.android.ui.bean;


/**
 * SearchDrugBean 药材
 * Create at 2018/4/27 上午10:20 by mayakun 
 */
public class SearchDrugBean {


    /**
     * id : 380
     * drug_name : 苍耳草
     * price : 0.01
     * unit : g
     * spec : g
     * use_flag : 1
     * datatype : 1
     * sub_drug_type 0：普通 1：精品
     */
    //搜索药材数据
    public int id;
    public String name;
    public String mcode;//药品唯一性code
    public String drug_type;//药品唯一类型（“ZY”：”中草药” “ZCY”：”中成药” “XY” ：”西药” “QC” ：”器材”）
    public double price;
    public String unit;
    public String spec;
    public int use_flag;
    //数据类型，1:药品，2：处方
    public int datatype;
    public int sub_drug_type;//0：普通 1：精品
    public String type_title;//处方的类型（处方）

}
