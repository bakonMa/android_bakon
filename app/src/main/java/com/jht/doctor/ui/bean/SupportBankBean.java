package com.jht.doctor.ui.bean;

/**
 * Created by table on 2017/12/12.
 * description:
 */

public class SupportBankBean {

    /**
     * bankName : string
     * createAt : 2017-12-12T06:03:42.113Z
     * createBy : string
     * deleteStatus : string
     * id : 0
     * singleAmt : 0
     * singleDayAmt : 0
     * singleMonthAmt : 0
     * updateAt : 2017-12-12T06:03:42.113Z
     * updateBy : string
     */

    private String bankName;
    private String createAt;
    private String createBy;
    private String deleteStatus;
    private int id;
    private double singleAmt;
    private double singleDayAmt;
    private double singleMonthAmt;
    private String updateAt;
    private String updateBy;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSingleAmt() {
        return singleAmt;
    }

    public void setSingleAmt(double singleAmt) {
        this.singleAmt = singleAmt;
    }

    public double getSingleDayAmt() {
        return singleDayAmt;
    }

    public void setSingleDayAmt(double singleDayAmt) {
        this.singleDayAmt = singleDayAmt;
    }

    public double getSingleMonthAmt() {
        return singleMonthAmt;
    }

    public void setSingleMonthAmt(double singleMonthAmt) {
        this.singleMonthAmt = singleMonthAmt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
