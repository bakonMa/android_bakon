package com.junhetang.doctor.ui.bean;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public class HomeLoanBean {

    /**
     * applyStatus : false
     * orderNo : string
     * preTrialAmt : 0
     * preTrialStatus : false
     * repaymentStatus : false
     */

    private boolean applyStatus;
    private String orderNo;
    private double preTrialAmt;
    private boolean preTrialStatus;
    private boolean repaymentStatus;

    public boolean isApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(boolean applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getPreTrialAmt() {
        return preTrialAmt;
    }

    public void setPreTrialAmt(double preTrialAmt) {
        this.preTrialAmt = preTrialAmt;
    }

    public boolean isPreTrialStatus() {
        return preTrialStatus;
    }

    public void setPreTrialStatus(boolean preTrialStatus) {
        this.preTrialStatus = preTrialStatus;
    }

    public boolean isRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(boolean repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }
}
