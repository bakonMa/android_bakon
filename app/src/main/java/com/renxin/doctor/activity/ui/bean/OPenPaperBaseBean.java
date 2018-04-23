package com.renxin.doctor.activity.ui.bean;

import java.util.List;

/**
 * OPenPaperBaseBean
 * Create at 2018/4/23 下午7:26 by mayakun
 */
public class OPenPaperBaseBean {

    public List<StoreBean> store;//药房
    public List<CommBean> drug_class;//
    public List<CommBean> usage;
    public List<CommBean> frequency;
    public List<CommBean> drugremark;

    public static class StoreBean {
        public int drug_store_id;
        public String drug_store_name;
    }

    public static class CommBean {
        public int id;
        public String name;
        public String ename;
    }


}
