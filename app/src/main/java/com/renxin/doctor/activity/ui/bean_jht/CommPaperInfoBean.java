package com.renxin.doctor.activity.ui.bean_jht;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * CommPaperInfoBean 常用方详情
 * Create at 2018/4/28 上午10:44 by mayakun 
 */
public class CommPaperInfoBean implements Parcelable{

    /**
     * drug_name	string	药品名称
     drug_num	int	用量
     decoction	string	煎法
     unit	string	单位
     spec	string	规格
     price	string	单价
     use_flag	int	1：能使用；0：不能使用
     */

    public int id;
    public String name;
    public int drug_num;
    public String decoction;
    public String unit;
    public String spec;
    public double price;
    public int use_flag;

    protected CommPaperInfoBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        drug_num = in.readInt();
        decoction = in.readString();
        unit = in.readString();
        spec = in.readString();
        price = in.readDouble();
        use_flag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(drug_num);
        dest.writeString(decoction);
        dest.writeString(unit);
        dest.writeString(spec);
        dest.writeDouble(price);
        dest.writeInt(use_flag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommPaperInfoBean> CREATOR = new Creator<CommPaperInfoBean>() {
        @Override
        public CommPaperInfoBean createFromParcel(Parcel in) {
            return new CommPaperInfoBean(in);
        }

        @Override
        public CommPaperInfoBean[] newArray(int size) {
            return new CommPaperInfoBean[size];
        }
    };
}
