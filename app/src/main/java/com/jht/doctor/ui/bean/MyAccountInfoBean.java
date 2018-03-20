package com.jht.doctor.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by table on 2017/12/16.
 * description:
 */

public class MyAccountInfoBean {

    /**
     * totalAvailableCredit : 0.0
     * totalRepayAmount : 6267.5
     * online : false
     * orderNo :
     * userAccountInfoDTO : [{"platformUserNo":"SLJR1031000000015865","userId":"0017395833988217","availableCredit":0,"frozenAmount":0,"totalAmount":0,"status":"1","userName":"张江","userPhone":"17395833988","orderNo":"","userBankName":"中国银行","userBankNo":"6217906101003163867","userRole":0,"idCardNo":"411528199012185092","totalRepaymentAmt":6267.5}]
     */

    private double totalAvailableCredit;
    private double totalRepayAmount;
    private boolean online;
    private String orderNo;
    private List<UserAccountInfoDTOBean> userAccountInfoDTO;

    public double getTotalAvailableCredit() {
        return totalAvailableCredit;
    }

    public void setTotalAvailableCredit(double totalAvailableCredit) {
        this.totalAvailableCredit = totalAvailableCredit;
    }

    public double getTotalRepayAmount() {
        return totalRepayAmount;
    }

    public void setTotalRepayAmount(double totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<UserAccountInfoDTOBean> getUserAccountInfoDTO() {
        return userAccountInfoDTO;
    }

    public void setUserAccountInfoDTO(List<UserAccountInfoDTOBean> userAccountInfoDTO) {
        this.userAccountInfoDTO = userAccountInfoDTO;
    }

    public static class UserAccountInfoDTOBean implements Parcelable{
        /**
         * platformUserNo : SLJR1031000000015865
         * userId : 0017395833988217
         * availableCredit : 0.0
         * frozenAmount : 0.0
         * totalAmount : 0.0
         * status : 1
         * userName : 张江
         * userPhone : 17395833988
         * orderNo :
         * userBankName : 中国银行
         * userBankNo : 6217906101003163867
         * userRole : 0
         * idCardNo : 411528199012185092
         * totalRepaymentAmt : 6267.5
         */

        private String platformUserNo;
        private String userId;
        private double availableCredit;
        private double frozenAmount;
        private double totalAmount;
        private String status;
        private String userName;
        private String userPhone;
        private String orderNo;
        private String userBankName;
        private String userBankNo;
        private int userRole;
        private String idCardNo;
        private double totalRepaymentAmt;

        protected UserAccountInfoDTOBean(Parcel in) {
            platformUserNo = in.readString();
            userId = in.readString();
            availableCredit = in.readDouble();
            frozenAmount = in.readDouble();
            totalAmount = in.readDouble();
            status = in.readString();
            userName = in.readString();
            userPhone = in.readString();
            orderNo = in.readString();
            userBankName = in.readString();
            userBankNo = in.readString();
            userRole = in.readInt();
            idCardNo = in.readString();
            totalRepaymentAmt = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(platformUserNo);
            dest.writeString(userId);
            dest.writeDouble(availableCredit);
            dest.writeDouble(frozenAmount);
            dest.writeDouble(totalAmount);
            dest.writeString(status);
            dest.writeString(userName);
            dest.writeString(userPhone);
            dest.writeString(orderNo);
            dest.writeString(userBankName);
            dest.writeString(userBankNo);
            dest.writeInt(userRole);
            dest.writeString(idCardNo);
            dest.writeDouble(totalRepaymentAmt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserAccountInfoDTOBean> CREATOR = new Creator<UserAccountInfoDTOBean>() {
            @Override
            public UserAccountInfoDTOBean createFromParcel(Parcel in) {
                return new UserAccountInfoDTOBean(in);
            }

            @Override
            public UserAccountInfoDTOBean[] newArray(int size) {
                return new UserAccountInfoDTOBean[size];
            }
        };

        public String getPlatformUserNo() {
            return platformUserNo;
        }

        public void setPlatformUserNo(String platformUserNo) {
            this.platformUserNo = platformUserNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getAvailableCredit() {
            return availableCredit;
        }

        public void setAvailableCredit(double availableCredit) {
            this.availableCredit = availableCredit;
        }

        public double getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(double frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getUserBankName() {
            return userBankName;
        }

        public void setUserBankName(String userBankName) {
            this.userBankName = userBankName;
        }

        public String getUserBankNo() {
            return userBankNo;
        }

        public void setUserBankNo(String userBankNo) {
            this.userBankNo = userBankNo;
        }

        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRole) {
            this.userRole = userRole;
        }

        public String getIdCardNo() {
            return idCardNo;
        }

        public void setIdCardNo(String idCardNo) {
            this.idCardNo = idCardNo;
        }

        public double getTotalRepaymentAmt() {
            return totalRepaymentAmt;
        }

        public void setTotalRepaymentAmt(double totalRepaymentAmt) {
            this.totalRepaymentAmt = totalRepaymentAmt;
        }
    }
}