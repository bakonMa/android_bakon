package com.jht.doctor.ui.bean;

import java.util.List;

/**
 * Created by table on 2017/12/11.
 * description:
 */

public class BankCardBean {


    /**
     * owner : {"id":22,"bankLogoUrl":"","bankName":"中国建设银行","bankNo":"6217001210093593828M","userName":"缪","userPhone":"18225877845","userIdCard":"340721199305213015","platformUserNo":"SLJR1031000000015106","platformChannel":"01","isBank":"3"}
     * joint : [{"id":24,"bankLogoUrl":"","bankName":"中国建设银行","bankNo":"6217001210093593828","userName":"姓名1","userPhone":"18896501234","userIdCard":"340721199305213015","platformUserNo":"SLJR1031000000015107","platformChannel":"01","isBank":"3"},{"id":0,"bankLogoUrl":"","bankName":null,"bankNo":null,"userName":"严武明","userPhone":"1888888888","userIdCard":"36043019910512197X","platformUserNo":null,"platformChannel":null,"isBank":"3"},{"id":0,"bankLogoUrl":"","bankName":null,"bankNo":null,"userName":"严武明","userPhone":"1888888888","userIdCard":"36043019910512197X","platformUserNo":null,"platformChannel":null,"isBank":"3"}]
     */

    private OwnerBean owner;
    private List<JointBean> joint;

    public OwnerBean getOwner() {
        return owner;
    }

    public void setOwner(OwnerBean owner) {
        this.owner = owner;
    }

    public List<JointBean> getJoint() {
        return joint;
    }

    public void setJoint(List<JointBean> joint) {
        this.joint = joint;
    }

    public static class OwnerBean {
        /**
         * id : 22
         * bankLogoUrl :
         * bankName : 中国建设银行
         * bankNo : 6217001210093593828M
         * userName : 缪
         * userPhone : 18225877845
         * userIdCard : 340721199305213015
         * platformUserNo : SLJR1031000000015106
         * platformChannel : 01
         * isBank : 3
         */

        private int id;
        private String bankLogoUrl;
        private String bankName;
        private String bankNo;
        private String userName;
        private String userPhone;
        private String userIdCard;
        private String platformUserNo;
        private String platformChannel;
        private String isBank;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBankLogoUrl() {
            return bankLogoUrl;
        }

        public void setBankLogoUrl(String bankLogoUrl) {
            this.bankLogoUrl = bankLogoUrl;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserIdCard() {
            return userIdCard;
        }

        public void setUserIdCard(String userIdCard) {
            this.userIdCard = userIdCard;
        }

        public String getPlatformUserNo() {
            return platformUserNo;
        }

        public void setPlatformUserNo(String platformUserNo) {
            this.platformUserNo = platformUserNo;
        }

        public String getPlatformChannel() {
            return platformChannel;
        }

        public void setPlatformChannel(String platformChannel) {
            this.platformChannel = platformChannel;
        }

        public String getIsBank() {
            return isBank;
        }

        public void setIsBank(String isBank) {
            this.isBank = isBank;
        }
    }

    public static class JointBean {
        /**
         * id : 24
         * bankLogoUrl :
         * bankName : 中国建设银行
         * bankNo : 6217001210093593828
         * userName : 姓名1
         * userPhone : 18896501234
         * userIdCard : 340721199305213015
         * platformUserNo : SLJR1031000000015107
         * platformChannel : 01
         * isBank : 3
         */

        private int id;
        private String bankLogoUrl;
        private String bankName;
        private String bankNo;
        private String userName;
        private String userPhone;
        private String userIdCard;
        private String platformUserNo;
        private String platformChannel;
        private String isBank;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBankLogoUrl() {
            return bankLogoUrl;
        }

        public void setBankLogoUrl(String bankLogoUrl) {
            this.bankLogoUrl = bankLogoUrl;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserIdCard() {
            return userIdCard;
        }

        public void setUserIdCard(String userIdCard) {
            this.userIdCard = userIdCard;
        }

        public String getPlatformUserNo() {
            return platformUserNo;
        }

        public void setPlatformUserNo(String platformUserNo) {
            this.platformUserNo = platformUserNo;
        }

        public String getPlatformChannel() {
            return platformChannel;
        }

        public void setPlatformChannel(String platformChannel) {
            this.platformChannel = platformChannel;
        }

        public String getIsBank() {
            return isBank;
        }

        public void setIsBank(String isBank) {
            this.isBank = isBank;
        }
    }
}
