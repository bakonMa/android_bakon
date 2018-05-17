package com.junhetang.doctor.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by table on 2017/12/12.
 * description:
 */

public class JudgeIfTiedBean implements Serializable{


    /**
     * orderNo : 小缪测试No号都别动
     * showOwnerBanner : true
     * showJointBanner : true
     * isBank :
     * data : [{"id":16,"orderNo":"小缪测试No号都别动","userName":"姓名1","bankCardNo":"","bankName":"","idCardNo":""}]
     */

    private String orderNo;
    private boolean showOwnerBanner;
    private boolean showJointBanner;
    private boolean showBanner;
    private String isBank;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 16
         * orderNo : 小缪测试No号都别动
         * userName : 姓名1
         * bankCardNo :
         * bankName :
         * idCardNo :
         */

        private int id;
        private String orderNo;
        private String userName;
        private String bankCardNo;
        private String bankName;
        private String bankMobilePhone;
        private String idCardNo;


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
