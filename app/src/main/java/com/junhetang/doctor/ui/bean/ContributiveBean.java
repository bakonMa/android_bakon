package com.junhetang.doctor.ui.bean;

/**
 * Created by table on 2018/1/12.
 * description:
 */

public class ContributiveBean {

    /**
     * bankCard : string
     * bankMobile : string
     * bankName : string
     * contributiveTypeJudge : false
     * haveBankCard : false
     * idCard : string
     * otherPlatformId : string
     * tradePwdStatus : false
     * userName : string
     */

    private String bankCard;
    private String bankMobile;
    private String bankName;
    private boolean contributiveTypeJudge;
    private boolean haveBankCard;
    private String idCard;
    private String otherPlatformId;
    private boolean tradePwdStatus;
    private String userName;

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isContributiveTypeJudge() {
        return contributiveTypeJudge;
    }

    public void setContributiveTypeJudge(boolean contributiveTypeJudge) {
        this.contributiveTypeJudge = contributiveTypeJudge;
    }

    public boolean isHaveBankCard() {
        return haveBankCard;
    }

    public void setHaveBankCard(boolean haveBankCard) {
        this.haveBankCard = haveBankCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOtherPlatformId() {
        return otherPlatformId;
    }

    public void setOtherPlatformId(String otherPlatformId) {
        this.otherPlatformId = otherPlatformId;
    }

    public boolean isTradePwdStatus() {
        return tradePwdStatus;
    }

    public void setTradePwdStatus(boolean tradePwdStatus) {
        this.tradePwdStatus = tradePwdStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
