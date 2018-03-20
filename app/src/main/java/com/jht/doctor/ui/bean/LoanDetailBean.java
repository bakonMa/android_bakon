package com.jht.doctor.ui.bean;

import java.util.List;

/**
 * Created by table on 2017/12/7.
 * description:
 */

public class LoanDetailBean {


    /**
     * finalRepaymentDate : string
     * loanAmt : 0
     * orderNo : string
     * orderStatus : string
     * outstandingAccountList : [{"overdueAmt":0,"period":0,"remissionAmt":0,"repaymentInterestAmt":0,"repaymentPrincipalAmt":0,"restReapaymenAmt":0,"serviceAmt":0,"totalPeriod":0,"totalRepaymentAmt":0}]
     * overdueAmtTotal : 0
     * overdueStatus : false
     * paidUpLoanList : [{"overdueAmt":0,"period":0,"remissionAmt":0,"repaymentInterestAmt":0,"repaymentPrincipalAmt":0,"restReapaymenAmt":0,"serviceAmt":0,"totalPeriod":0,"totalRepaymentAmt":0}]
     * periodNumber : 0
     * repaymentInterestTotal : 0
     * repaymentType : string
     * serviceAmtTotal : 0
     * cancelStatus:false
     * waitPaymentList : [{"overdueAmt":0,"period":0,"remissionAmt":0,"repaymentInterestAmt":0,"repaymentPrincipalAmt":0,"restReapaymenAmt":0,"serviceAmt":0,"totalPeriod":0,"totalRepaymentAmt":0}]
     */

    private String finalRepaymentDate;
    private double loanAmt;
    private String orderNo;
    private String orderStatus;
    private double overdueAmtTotal;
    private boolean overdueStatus;
    private int periodNumber;
    private double repaymentInterestTotal;
    private boolean cancelStatus;
    private String repaymentType;
    private double serviceAmtTotal;
    private List<OutstandingAccountListBean> outstandingAccountList;
    private List<PaidUpLoanListBean> paidUpLoanList;
    private List<WaitPaymentListBean> waitPaymentList;
    private double currentRepaymentAmt;
    private boolean contributiveTypeJudge;

    public boolean isContributiveTypeJudge() {
        return contributiveTypeJudge;
    }

    public void setContributiveTypeJudge(boolean contributiveTypeJudge) {
        this.contributiveTypeJudge = contributiveTypeJudge;
    }

    public double getCurrentRepaymentAmt() {
        return currentRepaymentAmt;
    }

    public void setCurrentRepaymentAmt(double currentRepaymentAmt) {
        this.currentRepaymentAmt = currentRepaymentAmt;
    }

    public String getFinalRepaymentDate() {
        return finalRepaymentDate;
    }

    public void setFinalRepaymentDate(String finalRepaymentDate) {
        this.finalRepaymentDate = finalRepaymentDate;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOverdueAmtTotal() {
        return overdueAmtTotal;
    }

    public void setOverdueAmtTotal(double overdueAmtTotal) {
        this.overdueAmtTotal = overdueAmtTotal;
    }

    public boolean isOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(boolean overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public double getRepaymentInterestTotal() {
        return repaymentInterestTotal;
    }

    public void setRepaymentInterestTotal(double repaymentInterestTotal) {
        this.repaymentInterestTotal = repaymentInterestTotal;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public double getServiceAmtTotal() {
        return serviceAmtTotal;
    }

    public void setServiceAmtTotal(double serviceAmtTotal) {
        this.serviceAmtTotal = serviceAmtTotal;
    }

    public List<OutstandingAccountListBean> getOutstandingAccountList() {
        return outstandingAccountList;
    }

    public void setOutstandingAccountList(List<OutstandingAccountListBean> outstandingAccountList) {
        this.outstandingAccountList = outstandingAccountList;
    }

    public List<PaidUpLoanListBean> getPaidUpLoanList() {
        return paidUpLoanList;
    }

    public void setPaidUpLoanList(List<PaidUpLoanListBean> paidUpLoanList) {
        this.paidUpLoanList = paidUpLoanList;
    }

    public List<WaitPaymentListBean> getWaitPaymentList() {
        return waitPaymentList;
    }

    public void setWaitPaymentList(List<WaitPaymentListBean> waitPaymentList) {
        this.waitPaymentList = waitPaymentList;
    }

    public static class OutstandingAccountListBean {
        /**
         * overdueAmt : 0
         * period : 0
         * remissionAmt : 0
         * repaymentInterestAmt : 0
         * repaymentPrincipalAmt : 0
         * restReapaymenAmt : 0
         * serviceAmt : 0
         * totalPeriod : 0
         * totalRepaymentAmt : 0
         */

        private double overdueAmt;
        private int period;
        private double remissionAmt;
        private double repaymentInterestAmt;
        private double repaymentPrincipalAmt;
        private double restReapaymenAmt;
        private double serviceAmt;
        private int totalPeriod;
        private double totalRepaymentAmt;

        public double getOverdueAmt() {
            return overdueAmt;
        }

        public void setOverdueAmt(double overdueAmt) {
            this.overdueAmt = overdueAmt;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public double getRemissionAmt() {
            return remissionAmt;
        }

        public void setRemissionAmt(double remissionAmt) {
            this.remissionAmt = remissionAmt;
        }

        public double getRepaymentInterestAmt() {
            return repaymentInterestAmt;
        }

        public void setRepaymentInterestAmt(double repaymentInterestAmt) {
            this.repaymentInterestAmt = repaymentInterestAmt;
        }

        public double getRepaymentPrincipalAmt() {
            return repaymentPrincipalAmt;
        }

        public void setRepaymentPrincipalAmt(double repaymentPrincipalAmt) {
            this.repaymentPrincipalAmt = repaymentPrincipalAmt;
        }

        public double getRestReapaymenAmt() {
            return restReapaymenAmt;
        }

        public void setRestReapaymenAmt(double restReapaymenAmt) {
            this.restReapaymenAmt = restReapaymenAmt;
        }

        public double getServiceAmt() {
            return serviceAmt;
        }

        public void setServiceAmt(double serviceAmt) {
            this.serviceAmt = serviceAmt;
        }

        public int getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(int totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public double getTotalRepaymentAmt() {
            return totalRepaymentAmt;
        }

        public void setTotalRepaymentAmt(double totalRepaymentAmt) {
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }

    public static class PaidUpLoanListBean {
        /**
         * overdueAmt : 0
         * period : 0
         * remissionAmt : 0
         * repaymentInterestAmt : 0
         * repaymentPrincipalAmt : 0
         * restReapaymenAmt : 0
         * serviceAmt : 0
         * totalPeriod : 0
         * totalRepaymentAmt : 0
         */

        private double overdueAmt;
        private int period;
        private double remissionAmt;
        private double repaymentInterestAmt;
        private double repaymentPrincipalAmt;
        private double restReapaymenAmt;
        private double serviceAmt;
        private int totalPeriod;
        private double totalRepaymentAmt;

        public double getOverdueAmt() {
            return overdueAmt;
        }

        public void setOverdueAmt(double overdueAmt) {
            this.overdueAmt = overdueAmt;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public double getRemissionAmt() {
            return remissionAmt;
        }

        public void setRemissionAmt(double remissionAmt) {
            this.remissionAmt = remissionAmt;
        }

        public double getRepaymentInterestAmt() {
            return repaymentInterestAmt;
        }

        public void setRepaymentInterestAmt(double repaymentInterestAmt) {
            this.repaymentInterestAmt = repaymentInterestAmt;
        }

        public double getRepaymentPrincipalAmt() {
            return repaymentPrincipalAmt;
        }

        public void setRepaymentPrincipalAmt(double repaymentPrincipalAmt) {
            this.repaymentPrincipalAmt = repaymentPrincipalAmt;
        }

        public double getRestReapaymenAmt() {
            return restReapaymenAmt;
        }

        public void setRestReapaymenAmt(double restReapaymenAmt) {
            this.restReapaymenAmt = restReapaymenAmt;
        }

        public double getServiceAmt() {
            return serviceAmt;
        }

        public void setServiceAmt(double serviceAmt) {
            this.serviceAmt = serviceAmt;
        }

        public int getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(int totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public double getTotalRepaymentAmt() {
            return totalRepaymentAmt;
        }

        public void setTotalRepaymentAmt(double totalRepaymentAmt) {
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }

    public static class WaitPaymentListBean {
        /**
         * overdueAmt : 0
         * period : 0
         * remissionAmt : 0
         * repaymentInterestAmt : 0
         * repaymentPrincipalAmt : 0
         * restReapaymenAmt : 0
         * serviceAmt : 0
         * totalPeriod : 0
         * totalRepaymentAmt : 0
         */

        private double overdueAmt;
        private int period;
        private double remissionAmt;
        private double repaymentInterestAmt;
        private double repaymentPrincipalAmt;
        private double restReapaymenAmt;
        private double serviceAmt;
        private int totalPeriod;
        private double totalRepaymentAmt;

        public double getOverdueAmt() {
            return overdueAmt;
        }

        public void setOverdueAmt(double overdueAmt) {
            this.overdueAmt = overdueAmt;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public double getRemissionAmt() {
            return remissionAmt;
        }

        public void setRemissionAmt(double remissionAmt) {
            this.remissionAmt = remissionAmt;
        }

        public double getRepaymentInterestAmt() {
            return repaymentInterestAmt;
        }

        public void setRepaymentInterestAmt(double repaymentInterestAmt) {
            this.repaymentInterestAmt = repaymentInterestAmt;
        }

        public double getRepaymentPrincipalAmt() {
            return repaymentPrincipalAmt;
        }

        public void setRepaymentPrincipalAmt(double repaymentPrincipalAmt) {
            this.repaymentPrincipalAmt = repaymentPrincipalAmt;
        }

        public double getRestReapaymenAmt() {
            return restReapaymenAmt;
        }

        public void setRestReapaymenAmt(double restReapaymenAmt) {
            this.restReapaymenAmt = restReapaymenAmt;
        }

        public double getServiceAmt() {
            return serviceAmt;
        }

        public void setServiceAmt(double serviceAmt) {
            this.serviceAmt = serviceAmt;
        }

        public int getTotalPeriod() {
            return totalPeriod;
        }

        public void setTotalPeriod(int totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public double getTotalRepaymentAmt() {
            return totalRepaymentAmt;
        }

        public void setTotalRepaymentAmt(double totalRepaymentAmt) {
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }
}
