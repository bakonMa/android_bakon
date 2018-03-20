package com.jht.doctor.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mayakun on 2017/12/11
 * 充值结果
 */

public class RechargeBean implements Parcelable{

    public double amount;//金额
    public String crmOrderNo;//CRM订单号
    public String platformUserNo;//平台用户账号id
    public String token;//oken 充值确认时带回


    protected RechargeBean(Parcel in) {
        amount = in.readDouble();
        crmOrderNo = in.readString();
        platformUserNo = in.readString();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeString(crmOrderNo);
        dest.writeString(platformUserNo);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RechargeBean> CREATOR = new Creator<RechargeBean>() {
        @Override
        public RechargeBean createFromParcel(Parcel in) {
            return new RechargeBean(in);
        }

        @Override
        public RechargeBean[] newArray(int size) {
            return new RechargeBean[size];
        }
    };
}
