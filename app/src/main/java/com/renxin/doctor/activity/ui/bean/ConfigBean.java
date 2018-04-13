package com.renxin.doctor.activity.ui.bean;

import java.util.List;

/**
 * Created by mayakun on 2017/12/4
 * 常量 配置信息
 */

public class ConfigBean {

    public List<ConfigItem> COMPANY_TYPE;
    public List<ConfigItem> HOUSE_TYPE;
    public List<ConfigItem> INDUSTRY;
    public List<ConfigItem> LOAN_USER;
    public List<ConfigItem> PERIOD_NUMBER;
    public List<ConfigItem> POSITION_LEVEL;
    public List<ConfigItem> REPAYMENT_TYPE;
    public List<ConfigItem> RELATION;

    public class ConfigItem {
        /**
         * id : 1
         * itemName : COMPANY_TYPE
         * itemVal : 01
         * colNameCn : 机关、事业
         * colName :
         * itemDesc : 公司类型
         */

        public int id;
        public String itemName;
        public String itemVal;
        public String colNameCn;
        public String colName;
        public String itemDesc;
    }

}
