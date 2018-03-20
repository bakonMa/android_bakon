package com.jht.doctor.ui.bean;

/**
 * Created by table on 2017/11/27.
 * description:
 */

public class HouseInfoResponse {

    /**
     * orderNo : string
     * preTrialAmt : 0
     * resultFlag : 0
     */

    private String orderNo;
    private double preTrialAmt;
    private int resultFlag;

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

    public int getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(int resultFlag) {
        this.resultFlag = resultFlag;
    }
}
