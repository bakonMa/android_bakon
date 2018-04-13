package com.renxin.doctor.activity.ui.bean;

/**
 * Created by table on 2017/12/15.
 * description:
 */

public class ReusingBean {


    /**
     * orderNo : (沪)20171214第001号
     * showOwnerBanner : true
     * showJointBanner : false
     * isBank : 03
     * data : {"id":31,"orderNo":"(沪)20171214第001号","userName":"马亚昆","bankCardNo":"","bankName":"","idCardNo":"412724199011094013"}
     */

    private String orderNo;
    private boolean showOwnerBanner;
    private boolean showJointBanner;
    private boolean showBanner;
    private String isBank;
    private DataBean data;

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isShowOwnerBanner() {
        return showOwnerBanner;
    }

    public void setShowOwnerBanner(boolean showOwnerBanner) {
        this.showOwnerBanner = showOwnerBanner;
    }

    public boolean isShowJointBanner() {
        return showJointBanner;
    }

    public void setShowJointBanner(boolean showJointBanner) {
        this.showJointBanner = showJointBanner;
    }

    public String getIsBank() {
        return isBank;
    }

    public void setIsBank(String isBank) {
        this.isBank = isBank;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 31
         * orderNo : (沪)20171214第001号
         * userName : 马亚昆
         * bankCardNo :
         * bankName :
         * idCardNo : 412724199011094013
         */

        private int id;
        private String orderNo;
        private String userName;
        private String bankCardNo;
        private String bankName;
        private String idCardNo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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
    }
}
