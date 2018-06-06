package com.junhetang.doctor.ui.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * JiuZhenHistoryBean 历史就诊人
 * Create at 2018/6/5 下午3:15 by mayakun
 */
public class JiuZhenHistoryBean implements Parcelable{

    public int id;
    public String patient_name;
    public int sex;//男：0、女：1
    public int age;
    public String phone;


    protected JiuZhenHistoryBean(Parcel in) {
        id = in.readInt();
        patient_name = in.readString();
        sex = in.readInt();
        age = in.readInt();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(patient_name);
        dest.writeInt(sex);
        dest.writeInt(age);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JiuZhenHistoryBean> CREATOR = new Creator<JiuZhenHistoryBean>() {
        @Override
        public JiuZhenHistoryBean createFromParcel(Parcel in) {
            return new JiuZhenHistoryBean(in);
        }

        @Override
        public JiuZhenHistoryBean[] newArray(int size) {
            return new JiuZhenHistoryBean[size];
        }
    };
}
