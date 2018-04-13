package com.renxin.doctor.activity.ui.bean;

import java.util.List;

/**
 * Created by mayakun on 2017/12/7
 * 线下还款bean
 */

public class RepaymentOffLineBean {

    public int currentRepaymentTotal;
    public List<RepaymentInfoBean> repaymentInfo;


    public static class RepaymentInfoBean {
//        orderStatus (string, optional): 订单状态 ,
//        overdueAmt (number, optional): 逾期违约金 ,
//        overdueStatus (boolean, optional): 是否逾期，true：逾期 false:未逾期 ,
//        period (integer, optional): 当前期数 ,
//        remissionAmt (number, optional): 违约金减免 ,
//        repaymentInterestAmt (number, optional): 还款利息 ,
//        repaymentPrincipalAmt (number, optional): 还款本金 ,
//        restReapaymenAmt (number, optional): 剩余应还 ,
//        serviceAmt (number, optional): 分期服务费 ,
//        totalPeriod (integer, optional): 总期数 ,
//        totalRepaymentAmt (number, optional): 应还总计

        public String orderStatus;
        public double overdueAmt;//预期违约金
        public boolean overdueStatus;//状态
        public int period;//当前期数
        public double remissionAmt;//违约减免
        public double repaymentInterestAmt;//还款利息
        public double repaymentPrincipalAmt;//还款本金
        public double restReapaymenAmt;//剩余应还
        public double serviceAmt;//分期服务费
        public int totalPeriod;//总期数
        public double totalRepaymentAmt;//应还总计

        public RepaymentInfoBean(String orderStatus, double overdueAmt, boolean overdueStatus, int period,
                                 double remissionAmt, double repaymentInterestAmt, double repaymentPrincipalAmt,
                                 double restReapaymenAmt, double serviceAmt, int totalPeriod, double totalRepaymentAmt) {
            this.orderStatus = orderStatus;
            this.overdueAmt = overdueAmt;
            this.overdueStatus = overdueStatus;
            this.period = period;
            this.remissionAmt = remissionAmt;
            this.repaymentInterestAmt = repaymentInterestAmt;
            this.repaymentPrincipalAmt = repaymentPrincipalAmt;
            this.restReapaymenAmt = restReapaymenAmt;
            this.serviceAmt = serviceAmt;
            this.totalPeriod = totalPeriod;
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }
}
