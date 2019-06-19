package com.bakon.android.ui.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * DrugBean 添加的药材
 * Create at 2018/4/27 上午10:20 by mayakun
 */
public class DrugBean implements Parcelable {
    //添加药材使用
    public int drug_id;
    public String drug_name;
    public String unit;//单位
    public String spec;//规格
    public String mcode;//药品唯一性code
    public String drug_type;//药品唯一类型（“ZY”：”中草药” "KLJ"：颗粒剂  “ZCY”：”中成药” “XY” ：”西药” “QC” ：”器材”）
    public int drug_num;//用量
    public double price;//价格
    public String decoction;//用法（常规）
    public int use_flag;//是否可用 1：能使用；0：不能使用
    public int sub_drug_type;//0：普通 1：精品

    public DrugBean() {
    }


    protected DrugBean(Parcel in) {
        drug_id = in.readInt();
        drug_name = in.readString();
        unit = in.readString();
        spec = in.readString();
        mcode = in.readString();
        drug_type = in.readString();
        drug_num = in.readInt();
        price = in.readDouble();
        decoction = in.readString();
        use_flag = in.readInt();
        sub_drug_type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drug_id);
        dest.writeString(drug_name);
        dest.writeString(unit);
        dest.writeString(spec);
        dest.writeString(mcode);
        dest.writeString(drug_type);
        dest.writeInt(drug_num);
        dest.writeDouble(price);
        dest.writeString(decoction);
        dest.writeInt(use_flag);
        dest.writeInt(sub_drug_type);
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
