package com.junhetang.doctor.ui.bean;

import java.util.List;

/**
 * PaperInfoBean 处方详情数据
 * Create at 2018/6/27 下午2:50 by mayakun
 */
public class PaperInfoBean {

    public String master_id;//暂未使用

    public int source;//来源：1：首页，2：聊天
    public String memb_no;//患者编号
    public String name;
    public int sex;//男：0、女：1
    public int memb_see;//0:不可见 1 可见
    public String age;
    public String phone;
    public int relationship;//与会员的关系0：本人 1：父母 2：子女 3：其他亲属 4：其他
    public String icd10;//主诉及辩证 病症描述
    public int store_id;//药房id
    public int drug_class;//剂型
    public int boiled_type;//代煎(0：代煎药1：自己煎药)
    public int drug_num;//副数
    public String usages;//用法
    public String freq;//用量
    public int service_price;//补充收费
    public String doc_remark;//医嘱
    public String remark;//备注
    public List<DrugBean> param;//药材

}
