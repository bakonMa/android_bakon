package com.bakon.android.ui.bean;


import java.util.List;

/**
 * CommPaperBean 常用处方列表
 * Create at 2018/4/28 上午10:44 by mayakun
 */
public class CommPaperBean {

    /**
     * id : 3
     * title : ceshi
     * m_explain : ceshi
     * title	string	名称
     * m_explain	string	备注
     */

    public int id;
    public int type;
    public int is_star;//经典处方显示星星 0:不亮 1：亮
    public String type_name;
    public String title;
    public String m_explain;
    public boolean isCheck;//view选择使用，默认false
    public List<DrugBean> druglist;//经典处方 药物详情
}
