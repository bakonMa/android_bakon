package com.junhetang.doctor.ui.bean;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public class MyLoanBean {

    /**
     * createTime : 2017-12-18
     * orderNo : (沪)20171218第028号
     * loanAmt : 280000.0
     * periodNumber : 6
     * orderStatus : 301
     * overdueStatus : false
     * creditStatus : false
     * userBankStatus : false
     * cancelStatus : false
     */

    private String createTime;
    private String orderNo;
    private double loanAmt;
    private int periodNumber;
    private String orderStatus;
    private boolean overdueStatus;
    private boolean creditStatus;
    private boolean userBankStatus;
    private boolean cancelStatus;
    private String repaymentDate;
    private double preTrialAmt;
    private double restRepaymentAmt;
    private double currentRepaymentAmt;

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public double getPreTrialAmt() {
        return preTrialAmt;
    }

    public void setPreTrialAmt(double preTrialAmt) {
        this.preTrialAmt = preTrialAmt;
    }

    public double getRestRepaymentAmt() {
        return restRepaymentAmt;
    }

    public void setRestRepaymentAmt(double restRepaymentAmt) {
        this.restRepaymentAmt = restRepaymentAmt;
    }

    public double getCurrentRepaymentAmt() {
        return currentRepaymentAmt;
    }

    public void setCurrentRepaymentAmt(double currentRepaymentAmt) {
        this.currentRepaymentAmt = currentRepaymentAmt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(boolean overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public boolean isCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(boolean creditStatus) {
        this.creditStatus = creditStatus;
    }

    public boolean isUserBankStatus() {
        return userBankStatus;
    }

    public void setUserBankStatus(boolean userBankStatus) {
        this.userBankStatus = userBankStatus;
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }
}
