package com.junhetang.doctor.ui.bean;

import java.util.List;

/**
 * Created by table on 2018/1/15.
 * description:
 */

public class IfBankOfJointBean {

    /**
     * data : [{"bankCardNo":"string","bankName":"string","idCardNo":"string","orderNo":"string","platformChannel":"string","userName":"string"}]
     * isBank : string
     * orderNo : string
     * showBanner : false
     * showJointBanner : false
     * showOwnerBanner : false
     */

    private String isBank;
    private String orderNo;
    private boolean showBanner;
    private boolean showJointBanner;
    private boolean showOwnerBanner;
    private List<DataBean> data;

    public String getIsBank() {
        return isBank;
    }

    public void setIsBank(String isBank) {
        this.isBank = isBank;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public boolean isShowJointBanner() {
        return showJointBanner;
    }

    public void setShowJointBanner(boolean showJointBanner) {
        this.showJointBanner = showJointBanner;
    }

    public boolean isShowOwnerBanner() {
        return showOwnerBanner;
    }

    public void setShowOwnerBanner(boolean showOwnerBanner) {
        this.showOwnerBanner = showOwnerBanner;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bankCardNo : string
         * bankName : string
         * idCardNo : string
         * orderNo : string
         * platformChannel : string
         * userName : string
         */
        private int id;
        private String bankCardNo;
        private String bankName;
        private String idCardNo;
        private String orderNo;
        private String bankMobilePhone;
        private String platformChannel;
        private String userName;

        public String getBankMobilePhone() {
            return bankMobilePhone;
        }

        public void setBankMobilePhone(String bankMobilePhone) {
            this.bankMobilePhone = bankMobilePhone;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBankCardNo() {
            return bankCardNo;
        }

        public void setBankCardNo(String bankCardNo) {
            this.bankCardNo = bankCardNo;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getIdCardNo() {
            return idCardNo;
        }

        public void setIdCardNo(String idCardNo) {
            this.idCardNo = idCardNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getPlatformChannel() {
            return platformChannel;
        }

        public void setPlatformChannel(String platformChannel) {
            this.platformChannel = platformChannel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
