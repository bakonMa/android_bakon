package com.renxin.doctor.activity.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BankCardBean 银行卡
 * Create at 2018/4/10 下午5:15 by mayakun 
 */

public class BankCardBean implements Parcelable{

    /**
     * id : 4
     * bank_number : ************1111
     * ch_name : 国家开发银行
     */

    public int id;
    public String bank_number;
    public String ch_name;

    protected BankCardBean(Parcel in) {
        id = in.readInt();
        bank_number = in.readString();
        ch_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(bank_number);
        dest.writeString(ch_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankCardBean> CREATOR = new Creator<BankCardBean>() {
        @Override
        public BankCardBean createFromParcel(Parcel in) {
            return new BankCardBean(in);
        }

        @Override
        public BankCardBean[] newArray(int size) {
            return new BankCardBean[size];
        }
    };
}
