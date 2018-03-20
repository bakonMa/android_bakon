package com.jht.doctor.ui.bean;

import java.util.List;

/**
 * Created by table on 2017/11/29.
 * description:
 */

public class RepaymentHomeBean {

    /**
     * currentRepaymentTotal : string
     * loanAmt : string
     * orderNo : string
     * payType : string
     * repaymentList : [{"overdueStatus":false,"period":"string","totalRepaymentAmt":"string","repaymentDate":"string","repaymentStatus":"string","totalPeriod":"string"}]
     * repaymentStatus : false
     * restReapaymenAmt : string
     */

    private String currentRepaymentTotal;
    private String loanAmt;
    private String orderNo;
    private String payType;
    private boolean repaymentStatus;
    private String restReapaymenAmt;
    private List<RepaymentListBean> repaymentList;

    public String getCurrentRepaymentTotal() {
        return currentRepaymentTotal;
    }

    public void setCurrentRepaymentTotal(String currentRepaymentTotal) {
        this.currentRepaymentTotal = currentRepaymentTotal;
    }

    public String getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(String loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public boolean isRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(boolean repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public String getRestReapaymenAmt() {
        return restReapaymenAmt;
    }

    public void setRestReapaymenAmt(String restReapaymenAmt) {
        this.restReapaymenAmt = restReapaymenAmt;
    }

    public List<RepaymentListBean> getRepaymentList() {
        return repaymentList;
    }

    public void setRepaymentList(List<RepaymentListBean> repaymentList) {
        this.repaymentList = repaymentList;
    }

    public static class RepaymentListBean {
        /**
         * overdueStatus : false
         * period : string
         * totalRepaymentAmt : string
         * repaymentDate : string
         * repaymentStatus : string
         * totalPeriod : string
         */

        private boolean overdueStatus;
        private String period;
        private String repaymentAmt;
        private String repaymentDate;
        private String repaymentStatus;
        private String totalPeriod;

        public boolean isOverdueStatus() {
            return overdueStatus;
        }

        public void setOverdueStatus(boolean overdueStatus) {
            this.overdueStatus = overdueStatus;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getRepaymentAmt() {
            return repaymentAmt;
        }

        public void setRepaymentAmt(String repaymentAmt) {
            this.repaymentAmt = repaymentAmt;
        }

        public String getRepaymentDate() {
            return repaymentDate;
        }

        public void setRepaymentDate(String repaymentDate) {
            this.repaymentDate = repaymentDate;
        }

        public String getRepaymentStatus() {
            return repaymentStatus;
        }

        public void setRepaymentStatus(String repaymentStatus) {
            this.repaymentStatus = repaymentStatus;
        }

        public String getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(String totalPeriod) {
            this.totalPeriod = totalPeriod;
        }
    }
}
