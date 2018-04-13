package com.renxin.doctor.activity.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayakun on 2017/11/27
 * 个人信息 bean
 */

public class MyInfoBean implements Parcelable {

    public UserDTOBean userDTO;
    public UserHouseDTOBean userHouseDTO;
    public UserJobDTOBean userJobDTO;


    //基本信息
    public static class UserDTOBean implements Parcelable {

        public String certNo;
        public String certType;
        public int id;
        public String userName;

        protected UserDTOBean(Parcel in) {
            certNo = in.readString();
            certType = in.readString();
            id = in.readInt();
            userName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(certNo);
            dest.writeString(certType);
            dest.writeInt(id);
            dest.writeString(userName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserDTOBean> CREATOR = new Creator<UserDTOBean>() {
            @Override
            public UserDTOBean createFromParcel(Parcel in) {
                return new UserDTOBean(in);
            }

            @Override
            public UserDTOBean[] newArray(int size) {
                return new UserDTOBean[size];
            }
        };
    }

    //工作信息
    public static class UserJobDTOBean implements Parcelable {

        public String companyName;
        public String companyType;
        public String industry;
        public String monthlyIncome;
        public String position;
        public int userId;

        protected UserJobDTOBean(Parcel in) {
            companyName = in.readString();
            companyType = in.readString();
            industry = in.readString();
            monthlyIncome = in.readString();
            position = in.readString();
            userId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(companyName);
            dest.writeString(companyType);
            dest.writeString(industry);
            dest.writeString(monthlyIncome);
            dest.writeString(position);
            dest.writeInt(userId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserJobDTOBean> CREATOR = new Creator<UserJobDTOBean>() {
            @Override
            public UserJobDTOBean createFromParcel(Parcel in) {
                return new UserJobDTOBean(in);
            }

            @Override
            public UserJobDTOBean[] newArray(int size) {
                return new UserJobDTOBean[size];
            }
        };
    }

    //房屋信息
    public static class UserHouseDTOBean implements Parcelable {

        public String areaCode;
        public String cityCode;
        public String communityName;
        public String detailAddress;
        public String hasLoan;
        public String houseArea;
        public String houseType;
        public double loanAmt;
        public double tradingAmt;//购买价格
        public double monthRentalAmount;//出租价格
        public String loanOrg;
        public String provinceCode;
        public int userId;


        protected UserHouseDTOBean(Parcel in) {
            areaCode = in.readString();
            cityCode = in.readString();
            communityName = in.readString();
            detailAddress = in.readString();
            hasLoan = in.readString();
            houseArea = in.readString();
            houseType = in.readString();
            loanAmt = in.readDouble();
            tradingAmt = in.readDouble();
            monthRentalAmount = in.readDouble();
            loanOrg = in.readString();
            provinceCode = in.readString();
            userId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(areaCode);
            dest.writeString(cityCode);
            dest.writeString(communityName);
            dest.writeString(detailAddress);
            dest.writeString(hasLoan);
            dest.writeString(houseArea);
            dest.writeString(houseType);
            dest.writeDouble(loanAmt);
            dest.writeDouble(tradingAmt);
            dest.writeDouble(monthRentalAmount);
            dest.writeString(loanOrg);
            dest.writeString(provinceCode);
            dest.writeInt(userId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserHouseDTOBean> CREATOR = new Creator<UserHouseDTOBean>() {
            @Override
            public UserHouseDTOBean createFromParcel(Parcel in) {
                return new UserHouseDTOBean(in);
            }

            @Override
            public UserHouseDTOBean[] newArray(int size) {
                return new UserHouseDTOBean[size];
            }
        };
    }

    protected MyInfoBean(Parcel in) {
        userDTO = in.readParcelable(UserDTOBean.class.getClassLoader());
        userHouseDTO = in.readParcelable(UserHouseDTOBean.class.getClassLoader());
        userJobDTO = in.readParcelable(UserJobDTOBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userDTO, flags);
        dest.writeParcelable(userHouseDTO, flags);
        dest.writeParcelable(userJobDTO, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyInfoBean> CREATOR = new Creator<MyInfoBean>() {
        @Override
        public MyInfoBean createFromParcel(Parcel in) {
            return new MyInfoBean(in);
        }

        @Override
        public MyInfoBean[] newArray(int size) {
            return new MyInfoBean[size];
        }
    };
}
