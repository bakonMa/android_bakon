package com.renxin.doctor.activity.ui.bean_jht;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * DrugBean 添加的药材
 * Create at 2018/4/27 上午10:20 by mayakun
 */
public class DrugBean implements Parcelable{
    //添加药材使用
    public int drug_id;
    public String name;
    public String unit;//单位
    public int drug_num;//用量
    public double price;//价格
    public String decoction;//用法（常规）
    public int use_flag;//是否可用 1：能使用；0：不能使用

    public DrugBean() {
    }

    protected DrugBean(Parcel in) {
        drug_id = in.readInt();
        name = in.readString();
        unit = in.readString();
        drug_num = in.readInt();
        price = in.readDouble();
        decoction = in.readString();
        use_flag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drug_id);
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeInt(drug_num);
        dest.writeDouble(price);
        dest.writeString(decoction);
        dest.writeInt(use_flag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DrugBean> CREATOR = new Creator<DrugBean>() {
        @Override
        public DrugBean createFromParcel(Parcel in) {
            return new DrugBean(in);
        }

        @Override
        public DrugBean[] newArray(int size) {
            return new DrugBean[size];
        }
    };
}
