package com.jht.doctor.ui.bean;

import java.util.List;

/**
 * Created by mayakun on 2017/12/19
 * 交易明细
 */

public class DealInfoBean {
    public int totalCount;
    public int pageNo;
    public int pageSize;
    public List<DealItemBean> data;

    public static class DealItemBean {

        public double amount;
        public String bankCardNo;
        public int commission;
        public String crmOrderNo;
        public int id;
        public String orderNo;
        public String orderType;
        public String orgCode;
        public String platformUserNo;
        public String rechargeWay;
        public String resCode;
        public String resMsg;
        public String resRequestNo;
        public String resStatus;
        public String updateAt;
        public int userId;
    }
}
